package com.mb.lab.banks.user.business.service.manager.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.mb.lab.banks.user.business.aop.RequireFeature;
import com.mb.lab.banks.user.business.dto.base.DTO;
import com.mb.lab.banks.user.business.dto.base.ListDto;
import com.mb.lab.banks.user.business.dto.base.ListDto.ListCountQuery;
import com.mb.lab.banks.user.business.dto.sms.SmsContentConfigDto;
import com.mb.lab.banks.user.business.dto.subadmin.UserDto;
import com.mb.lab.banks.user.business.dto.subadmin.UserWriteDto;
import com.mb.lab.banks.user.business.dto.user.UserSearchParamDto;
import com.mb.lab.banks.user.business.dto.user.UserSimpleDto;
import com.mb.lab.banks.user.business.service.base.impl.PODraftableServiceImpl;
import com.mb.lab.banks.user.business.service.manager.SubAdminManagementService;
import com.mb.lab.banks.user.business.service.sms.InternalSmsService;
import com.mb.lab.banks.user.business.service.sub.UserFeatureCacheSubService;
import com.mb.lab.banks.user.persistence.domain.User;
import com.mb.lab.banks.user.persistence.domain.UserFeature;
import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;
import com.mb.lab.banks.user.persistence.domain.entity.Feature;
import com.mb.lab.banks.user.persistence.domain.entity.SmsType;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;
import com.mb.lab.banks.user.persistence.repository.PODraftableRepository;
import com.mb.lab.banks.user.persistence.repository.UserRepository;
import com.mb.lab.banks.user.util.common.PasswordUtils;
import com.mb.lab.banks.user.util.security.UserLogin;
import com.mb.lab.banks.utils.common.MobileNumberUtils;
import com.mb.lab.banks.utils.event.SendSmsEvent;
import com.mb.lab.banks.utils.event.UserDeactivationEvent;
import com.mb.lab.banks.utils.event.UserPasswordChangedEvent;
import com.mb.lab.banks.utils.event.stream.EventStreamsHelper;
import com.mb.lab.banks.utils.event.stream.SendSmsStreams;
import com.mb.lab.banks.utils.event.stream.UserDeactivationStreams;
import com.mb.lab.banks.utils.event.stream.UserPasswordChangedStreams;
import com.mb.lab.banks.utils.exception.BusinessAssert;
import com.mb.lab.banks.utils.exception.BusinessExceptionCode;

@RequireFeature({ Feature.SUB_ADMIN_MANAGEMENT })
@Transactional
@Service
public class SubAdminManagementServiceImpl extends PODraftableServiceImpl<User, UserDto, UserWriteDto>
		implements SubAdminManagementService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EventStreamsHelper eventStreamsHelper;

	@Autowired
	private UserDeactivationStreams.OutBound userDeactivationStreamsOutBound;

	@Autowired
	private UserPasswordChangedStreams.OutBound userPasswordChangedStreamsOutBound;

	@Autowired
	private SendSmsStreams.OutBound sendSmsStreamsOutBound;

	@Autowired
	private InternalSmsService internalSmsService;

	@Autowired
	private UserFeatureCacheSubService userFeatureCacheSubService;

	@Override
	protected PODraftableRepository<User> getRepository() {
		return userRepository;
	}

	@Override
	protected UserDto convertToReadDto(User domain) {
		Set<UserFeature> userFeatures = domain.getFeatures();
		Set<Feature> features = new HashSet<>(userFeatures.size());
		for (UserFeature userFeature : userFeatures) {
			features.add(userFeature.getFeature());
		}
		return new UserDto(domain, features);
	}

	@Override
	protected boolean isDomainValid(UserLogin userLogin, User domain) {
		return domain.getRole() != null && domain.getRole().equals(UserRole.SUB_ADMIN);
	}

	@Override
	public UserDto getById(UserLogin userLogin, Long id) {
		return getByIdSupport(userLogin, id);
	}

	@Override
	public void checkExist(UserLogin userLogin, Long id) {
		checkExistSupport(userLogin, id);
	}

	@Override
	public void activate(UserLogin userLogin, Long id) {
		activateSupport(userLogin, id);
	}

	@Override
	public void deactivate(UserLogin userLogin, Long id) {
		deactivateSupport(userLogin, id);
	}

	@Override
	public ListDto<UserSimpleDto> search(UserLogin userLogin, UserSearchParamDto searchParam) {
		ActiveStatus activeStatus = searchParam == null ? null : searchParam.getActiveStatus();
		String username = searchParam == null ? null : searchParam.getUsername();
		String keyword = searchParam == null ? null : searchParam.getUsername();
		Pageable pageable = getPageRequest(searchParam);

		return ListDto.get(new ListCountQuery<User, UserSimpleDto>() {

			@Override
			public List<? extends User> getList() {
				return userRepository.findByRole(UserRole.SUB_ADMIN, activeStatus, username, keyword, null, null, null,
						null, pageable);
			}

			@Override
			public long count() {
				return userRepository.countByRole(UserRole.SUB_ADMIN, activeStatus, username, keyword, null, null, null,
						null);
			}

			@Override
			public UserSimpleDto convert(User domain) {
				return new UserSimpleDto(domain);
			}
		}, pageable);
	}

	@Override
	public DTO create(UserLogin userLogin, UserWriteDto writeDto) {
		BusinessAssert.notNull(writeDto);

		String newPassword = PasswordUtils.generatePassword();

		String fullname = getValidText(writeDto.getFullname(), true, 1, 100);
		String username = getValidUsername(null, writeDto.getUsername()).toLowerCase();
		String phone = getValidText(writeDto.getPhone(), true, 1, 20);
		String msisdnStandardized = MobileNumberUtils.mobileNumberToMsisdn(phone);
		User domain = new User();
		domain.setRole(UserRole.SUB_ADMIN);
		domain.setFullname(fullname);
		domain.setUsername(username);
		domain.setPhone(msisdnStandardized);
		domain.setEmail(getValidEmail(writeDto.getEmail(), false, 255));
		domain.setPassword(passwordEncoder.encode(newPassword));
		domain.setActiveStatus(ActiveStatus.ACTIVE);

		Collection<Feature> subAdminFeatures = Feature.SUB_ADMIN_FEATURE_SET.getFeatures();

		Set<UserFeature> features = new HashSet<>();
		for (Feature feature : writeDto.getFeatures()) {
			BusinessAssert.isTrue(subAdminFeatures.contains(feature));
			UserFeature userFeature = new UserFeature();
			userFeature.setFeature(feature);
			userFeature.setUser(domain);

			features.add(userFeature);
		}

		domain.setFeatures(features);

		domain = getRepository().save(domain);

		// Send SMS
		SmsContentConfigDto template = internalSmsService.getContentConfig(SmsType.CREATE_SUB_ADMIN);

		if (template == null) {
			logger.info("No sms content config ACTIVE for type " + SmsType.CREATE_SUB_ADMIN);
		} else {
			String smsContent = createSmsContent(template.getContent(), domain, newPassword);

			SendSmsEvent event = SendSmsEvent.create(msisdnStandardized, smsContent);

			eventStreamsHelper.sendEvent(sendSmsStreamsOutBound.channel(), event);
		}

		return new DTO(domain);
	}

	@Override
	public DTO update(UserLogin userLogin, Long userId, UserWriteDto writeDto) {
		BusinessAssert.notNull(writeDto);

		User domain = getPOMandatory(getRepository(), userId);
		Set<UserFeature> currentUserFeatures = domain.getFeatures();

		BusinessAssert.isTrue(isDomainValid(userLogin, domain));
		BusinessAssert.isTrue(!domain.isInactive());
		String phone = getValidText(writeDto.getPhone(), true, 1, 20);
		String msisdnStandardized = MobileNumberUtils.mobileNumberToMsisdn(phone);
		domain.setFullname(getValidText(writeDto.getFullname(), true, 1, 100));
		domain.setPhone(msisdnStandardized);
		domain.setEmail(getValidEmail(writeDto.getEmail(), false, 255));

		Collection<Feature> subAdminFeatures = Feature.SUB_ADMIN_FEATURE_SET.getFeatures();

		if (CollectionUtils.isEmpty(writeDto.getFeatures())) {
			currentUserFeatures.clear();
		} else {
			Set<Feature> newFeatures = writeDto.getFeatures();

			Map<Feature, UserFeature> currentFeatureMap = new HashMap<>(currentUserFeatures.size());
			for (UserFeature userFeature : currentUserFeatures) {
				currentFeatureMap.put(userFeature.getFeature(), userFeature);
			}

			// Find deleted partners
			Set<UserFeature> deleted = new HashSet<>();
			for (UserFeature userFeature : currentUserFeatures) {
				if (!newFeatures.contains(userFeature.getFeature())) {
					deleted.add(userFeature);
				}
			}
			// Find and check status of added partners
			Set<UserFeature> added = new HashSet<>();
			for (Feature feature : newFeatures) {
				BusinessAssert.isTrue(subAdminFeatures.contains(feature));
				if (!currentFeatureMap.containsKey(feature)) {
					UserFeature userFeature = new UserFeature();
					userFeature.setUser(domain);
					userFeature.setFeature(feature);

					added.add(userFeature);
				}
			}

			currentUserFeatures.removeAll(deleted);
			currentUserFeatures.addAll(added);
		}

		domain = getRepository().save(domain);

		// Invalidate feature cache
		userFeatureCacheSubService.invalidate(domain.getId());

		return new DTO(domain);
	}

	@Override
	public void resetPassword(UserLogin userLogin, Long userId) {
		BusinessAssert.isTrue(this.isAdmin(userLogin), "Only admin can reset password");

		User user = getPOMandatory(userRepository, userId);
		BusinessAssert.isTrue(!user.isSystemUser(), "Cannot reset password of system users");
		BusinessAssert.isTrue(user.getRole().equals(UserRole.SUB_ADMIN));

		BusinessAssert.isTrue(isDomainValid(userLogin, user));

		String newPassword = PasswordUtils.generatePassword();
		user.setPassword(passwordEncoder.encode(newPassword));

		userRepository.save(user);

		// Send SMS
		SmsContentConfigDto template = internalSmsService.getContentConfig(SmsType.RESET_PASSWORD_SUB_ADMIN);

		if (template == null) {
			logger.info("No sms content config ACTIVE for type " + SmsType.RESET_PASSWORD_SUB_ADMIN);
		} else {
			String smsContent = createSmsContent(template.getContent(), user, newPassword);

			SendSmsEvent event = SendSmsEvent.create(user.getPhone(), smsContent);

			eventStreamsHelper.sendEvent(sendSmsStreamsOutBound.channel(), event);
		}

		eventStreamsHelper.sendEvent(userPasswordChangedStreamsOutBound.channel(),
				new UserPasswordChangedEvent(user.getId()));
	}

	// PROTECTED
	@Override
	protected void afterDeactivate(UserLogin userLogin, User domain) {
		super.afterDeactivate(userLogin, domain);

		eventStreamsHelper.sendEvent(userDeactivationStreamsOutBound.channel(),
				new UserDeactivationEvent(domain.getId()));
	}

	// PRIVATE
	private String getValidUsername(Long userId, String username) {
		username = getValidText(username, true, 3, 20);

		BusinessAssert.isTrue(VALID_USERNAME_REGEX.matcher(username).find());

		Optional<User> optional = userRepository.findByUsername(username);

		boolean isUsed = optional.isPresent() && (userId == null || !optional.get().getId().equals(userId));
		BusinessAssert.isTrue(!isUsed, BusinessExceptionCode.USERNAME_USED, "username used");

		return username;
	}

	private String createSmsContent(String template, User user, String password) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("fullname", user.getFullname());
		data.put("username", user.getUsername());
		data.put("password", password);

		return StringSubstitutor.replace(template, data);
	}

}
