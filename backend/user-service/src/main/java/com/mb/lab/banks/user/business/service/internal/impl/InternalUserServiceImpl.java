package com.mb.lab.banks.user.business.service.internal.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.mb.lab.banks.user.business.dto.login.ChangePasswordDto;
import com.mb.lab.banks.user.business.dto.login.UserInfoDto;
import com.mb.lab.banks.user.business.dto.login.UserLoginDto;
import com.mb.lab.banks.user.business.dto.user.UserSearchParamDto;
import com.mb.lab.banks.user.business.dto.user.UserSimpleDto;
import com.mb.lab.banks.user.business.service.A_Service;
import com.mb.lab.banks.user.business.service.internal.InternalUserService;
import com.mb.lab.banks.user.config.PasswordStrenghtProperties;
import com.mb.lab.banks.user.persistence.domain.User;
import com.mb.lab.banks.user.persistence.domain.UserFeature;
import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;
import com.mb.lab.banks.user.persistence.domain.entity.Feature;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;
import com.mb.lab.banks.user.persistence.repository.UserFeatureRepository;
import com.mb.lab.banks.user.persistence.repository.UserRepository;
import com.mb.lab.banks.user.util.common.PasswordUtils;
import com.mb.lab.banks.utils.common.StringUtils;
import com.mb.lab.banks.utils.exception.BusinessAssert;
import com.mb.lab.banks.utils.exception.BusinessExceptionCode;

@Transactional
@Service
public class InternalUserServiceImpl extends A_Service implements InternalUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserFeatureRepository userFeatureRepository;

	@Autowired
	private PasswordStrenghtProperties passwordStrenghtProperties;

	@Override
	public UserLoginDto getByUsername(String username) {
		BusinessAssert.isTrue(!StringUtils.isEmpty(username));

		Optional<User> optional = userRepository.findByUsername(username.toLowerCase());

		if (optional.isPresent()) {
			return new UserLoginDto(optional.get());
		}

		return null;
	}

	@Override
	public UserInfoDto getUserInfo(Long userId) {
		Optional<User> optional = userRepository.findById(userId);
		if (optional.isPresent()) {
			User user = optional.get();

			if (user != null) {
				if (user.getRole().equals(UserRole.ADMIN) || user.getRole().equals(UserRole.ECOM_ADMIN)) {
					return new UserInfoDto(user);
				}

				List<UserFeature> userFeatures = userFeatureRepository.findByUserId(user.getId());
				Set<Feature> features = new HashSet<>(userFeatures.size());
				for (UserFeature userFeature : userFeatures) {
					features.add(userFeature.getFeature());
				}

				return new UserInfoDto(user, features);
			}
		}

		return null;
	}

	@Override
	public void changePassword(Long userId, ChangePasswordDto dto) {
		BusinessAssert.notNull(dto);
		BusinessAssert.notNull(dto.getOldPassword());
		BusinessAssert.notNull(dto.getNewPassword());
		BusinessAssert.isTrue(!dto.getOldPassword().toLowerCase().equals(dto.getNewPassword().toLowerCase()),
				BusinessExceptionCode.PASSWORD_NOT_SAME, "password not same");
		List<String> weakPasswordPatterns = passwordStrenghtProperties.getWeakPassword();
		if (!StringUtils.isEmpty(weakPasswordPatterns)) {
			BusinessAssert.isTrue(!PasswordUtils.isWeakPassword(dto.getNewPassword(), weakPasswordPatterns),
					BusinessExceptionCode.WEAK_PASSWORD, "weak password");
		}

		BusinessAssert.isTrue(PasswordUtils.isValidPassword(dto.getNewPassword()),
				BusinessExceptionCode.INVALID_NEW_PASSWORD, "Password not strong enough");

		Optional<User> optional = userRepository.findById(userId);

		BusinessAssert.isTrue(optional.isPresent(), "No user found");

		User user = optional.get();

		BusinessAssert.isTrue(passwordEncoder.matches(dto.getOldPassword(), user.getPassword()),
				BusinessExceptionCode.INVALID_OLD_PASSWORD, "Old password not match");

		user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

		userRepository.save(user);
	}

	@Override
	public UserSimpleDto getById(Long id) {
		User user = getPOMandatory(userRepository, id);
		return new UserSimpleDto(user);
	}

	@Override
	public List<UserSimpleDto> getList(UserSearchParamDto searchParam) {
		Assert.notNull(searchParam, "Search param must not be null");
		ActiveStatus activeStatus = searchParam.getActiveStatus();

		List<User> domains = userRepository.findByRole(null, activeStatus, null, null, null, null,
				searchParam.getIdList(), searchParam.getUsernameList(), null);
		if (CollectionUtils.isEmpty(domains)) {
			return Collections.emptyList();
		}

		List<UserSimpleDto> dtos = new ArrayList<>(domains.size());
		for (User domain : domains) {
			dtos.add(new UserSimpleDto(domain));
		}

		return dtos;
	}

	@Override
	public void deactivateAdminPartner(Long partnerId) {
		List<User> domains = userRepository.findByRole(UserRole.PARTNER_ADMIN, ActiveStatus.ACTIVE, null, null, null,
				partnerId, null, null, null);
		if (CollectionUtils.isEmpty(domains)) {
			return;
		}
		Set<Long> idList = new HashSet<>();
		for (User domain : domains) {
			idList.add(domain.getId());
		}
		userRepository.deactivate(idList);
	}

	@Override
	public void deactivateAdminStore(Long storeId) {
		List<User> domains = userRepository.findByRole(UserRole.STORE_ADMIN, ActiveStatus.ACTIVE, null, null, storeId,
				null, null, null, null);
		if (CollectionUtils.isEmpty(domains)) {
			return;
		}
		Set<Long> idList = new HashSet<>();
		for (User domain : domains) {
			idList.add(domain.getId());
		}
		userRepository.deactivate(idList);
	}

	@Override
	public Set<String> getFeatures(Long id) {
		List<UserFeature> userFeatures = userFeatureRepository.findByUserId(id);
		Set<String> features = new HashSet<>(userFeatures.size());
		for (UserFeature userFeature : userFeatures) {
			features.add(userFeature.getFeature().name());
		}
		return features;
	}

}
