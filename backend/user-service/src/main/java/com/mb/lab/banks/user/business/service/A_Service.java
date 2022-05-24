package com.mb.lab.banks.user.business.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.mb.lab.banks.user.business.dto.base.DTO;
import com.mb.lab.banks.user.business.dto.base.PageNumberRequestDto;
import com.mb.lab.banks.user.business.service.sub.PhotoFactory;
import com.mb.lab.banks.user.persistence.domain.base.PO;
import com.mb.lab.banks.user.persistence.domain.base.PODraftable;
import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;
import com.mb.lab.banks.user.persistence.repository.PODraftableRepository;
import com.mb.lab.banks.user.persistence.repository.PORepository;
import com.mb.lab.banks.user.util.common.ExcelUtils.ExcelErrorMessageFactory;
import com.mb.lab.banks.user.util.security.UserLogin;
import com.mb.lab.banks.utils.common.DateTimeUtils;
import com.mb.lab.banks.utils.common.StringUtils;
import com.mb.lab.banks.utils.exception.BusinessAssert;
import com.mb.lab.banks.utils.exception.BusinessException;
import com.mb.lab.banks.utils.exception.BusinessExceptionCode;

public abstract class A_Service {

    protected static final int PAGE_SIZE_DEFAULT = 20;
    protected static final Pattern VALID_CODE_REGEX = Pattern.compile("^[a-zA-Z0-9_-]+$", Pattern.CASE_INSENSITIVE);
    protected static final Pattern VALID_USERNAME_REGEX = Pattern.compile("^[0-9_a-z]{3,20}$", Pattern.CASE_INSENSITIVE);
    protected static final Pattern VALID_PHONE_REGEX = Pattern.compile("^[0-9]{9,20}$", Pattern.CASE_INSENSITIVE);
    protected static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    protected static final Pattern LINK_REGEX = Pattern.compile("^https?:\\/\\/[a-z0-9]+([\\-.][a-z0-9]+)*(:[0-9]{1,5})?(\\/.*)?$", Pattern.CASE_INSENSITIVE);
    protected static final String DEFAULT_LANG = "vi";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhotoFactory photoFactory;

    @Autowired
    private MessageSource messageSource;

    /*-----FACTORY-----*/
    protected PhotoFactory getPhotoFactory() {
        return photoFactory;
    }

    /*-----VALIDATION-----*/
    protected <T extends PO> T getPOMandatory(PORepository<T> repository, Long id) {
        BusinessAssert.notEmptyText(id);
        Optional<T> optional = repository.findById(id);
        BusinessAssert.isTrue(optional.isPresent());
        return optional.get();
    }

    protected <T extends PODraftable> T getPOEnabledMandatory(PODraftableRepository<T> repository, Long id) {
        BusinessAssert.notEmptyText(id);
        Optional<T> optional = repository.findById(id);
        BusinessAssert.isTrue(optional.isPresent());
        T domain = optional.get();
        BusinessAssert.isTrue(!domain.getActiveStatus().equals(ActiveStatus.DRAFT));
        return domain;
    }

    protected <T extends PODraftable> T getPOActivatedMandatory(PODraftableRepository<T> repository, Long id) {
        BusinessAssert.notEmptyText(id);
        Optional<T> optional = repository.findById(id);
        BusinessAssert.isTrue(optional.isPresent());
        T domain = optional.get();
        BusinessAssert.isTrue(domain.getActiveStatus().equals(ActiveStatus.ACTIVE));
        return domain;
    }

    protected Date getMandatoryIsoDate(String isoDate) {
        BusinessAssert.notNull(isoDate);
        try {
            return DateTimeUtils.getDateFromIsoDate(isoDate);
        } catch (ParseException e) {
            throw new BusinessException(BusinessExceptionCode.INVALID_PARAM, "iso date wrong format");
        }
    }

    protected Date getMandatoryIsoTime(String isoTime) {
        BusinessAssert.notNull(isoTime);
        try {
            return DateTimeUtils.getDateFromIsoTime(isoTime);
        } catch (ParseException e) {
            throw new BusinessException(BusinessExceptionCode.INVALID_PARAM, "iso time wrong format");
        }
    }

    protected String getValidText(String text, boolean mandatory, int min, int max) {
        Assert.isTrue(min > 0 && max >= min, "min max incorrect");

        if (mandatory) {
            BusinessAssert.isTrue(!StringUtils.isEmpty(text));
        }

        if (!StringUtils.isEmpty(text)) {
            text = text.trim();
            BusinessAssert.isTrue(text.length() >= min && text.length() <= max);
            return text;
        }

        return null;
    }

    protected BigDecimal getValidNumber(BigDecimal number,
            boolean mandatory,
            int scale,
            BigDecimal min,
            boolean includeMin,
            BigDecimal max,
            boolean includeMax) {
        if (max != null && min != null) {
            Assert.isTrue(max.compareTo(min) >= 0, "min max incorrect");
        }

        if (mandatory) {
            BusinessAssert.notNull(number);
        }

        if (number != null) {
            number = number.setScale(scale, RoundingMode.HALF_UP);

            if (min != null) {
                BusinessAssert.isTrue(number.compareTo(min) > 0 || (includeMin && number.compareTo(min) == 0));
            }

            if (max != null) {
                BusinessAssert.isTrue(number.compareTo(max) < 0 || (includeMax && number.compareTo(max) == 0));
            }

            return number;
        }

        return null;
    }

    protected BigDecimal getValidNumber(BigDecimal number, boolean mandatory, int scale, BigDecimal min, BigDecimal max) {
        return getValidNumber(number, mandatory, scale, min, true, max, true);
    }

    protected BigDecimal getValidNumber(BigDecimal number, boolean mandatory, Integer min, Integer max) {
        return getValidNumber(number, mandatory, 0, min == null ? null : new BigDecimal(min), max == null ? null : new BigDecimal(max));
    }

    protected Integer getValidNumber(Integer number, boolean mandatory, Integer min, Integer max) {
        Assert.notNull(min, "min max incorrect");
        max = max == null ? Integer.MAX_VALUE : max;
        Assert.isTrue(max >= min, "min max incorrect");

        if (mandatory) {
            BusinessAssert.notNull(number);
        }

        if (number != null) {
            BusinessAssert.isTrue(number.compareTo(min) >= 0 && number.compareTo(max) <= 0);
            return number;
        }

        return null;
    }

    protected String getValidEmail(String email, boolean mandatory, int maxLength) {
        if (mandatory) {
            BusinessAssert.isTrue(!StringUtils.isEmpty(email), BusinessExceptionCode.INVALID_EMAIL, "email null");
        }

        if (!StringUtils.isEmpty(email)) {
            email = email.trim().toLowerCase();
            BusinessAssert.isTrue(email.length() <= maxLength);

            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
            BusinessAssert.isTrue(matcher.find(), BusinessExceptionCode.INVALID_EMAIL, "email invalid");

            return email;
        }

        return null;
    }

    protected String getValidUsername(String username, boolean mandatory, int minLength, int maxLength) {
        if (mandatory) {
            BusinessAssert.isTrue(!StringUtils.isEmpty(username), BusinessExceptionCode.INVALID_USERNAME, "username null");
        }

        if (!StringUtils.isEmpty(username)) {
            username = username.trim().toLowerCase();
            BusinessAssert.isTrue(username.length() >= minLength && username.length() <= maxLength);

            Matcher matcher = VALID_USERNAME_REGEX.matcher(username);
            BusinessAssert.isTrue(matcher.find(), BusinessExceptionCode.INVALID_USERNAME, "username invalid");

            return username;
        }

        return null;
    }

    protected String getValidPhone(String phone, boolean mandatory, int maxLength) {
        if (mandatory) {
            BusinessAssert.isTrue(!StringUtils.isEmpty(phone), BusinessExceptionCode.INVALID_PHONE, "phone null");
        }

        if (!StringUtils.isEmpty(phone)) {
            phone = phone.trim().toLowerCase();
            BusinessAssert.isTrue(phone.length() <= maxLength);

            Matcher matcher = VALID_PHONE_REGEX.matcher(phone);
            BusinessAssert.isTrue(matcher.find(), BusinessExceptionCode.INVALID_PHONE, "phone invalid");

            return phone;
        }

        return null;
    }

    protected String getValidLink(String link, boolean mandatory, int maxLength) {
        if (mandatory) {
            BusinessAssert.isTrue(!StringUtils.isEmpty(link), BusinessExceptionCode.INVALID_LINK, "link null");
        }

        if (!StringUtils.isEmpty(link)) {
            link = link.trim();
            BusinessAssert.isTrue(link.length() <= maxLength);

            Matcher matcher = LINK_REGEX.matcher(link);
            BusinessAssert.isTrue(matcher.find(), BusinessExceptionCode.INVALID_EMAIL, "link invalid");

            return link;
        }

        return null;
    }

    protected String getValidLang(String lang) {
        if (StringUtils.isEmpty(lang)) {
            return DEFAULT_LANG;
        }

        switch (lang) {
            case "vi":
                return "vi";
            case "vn":
                return "vi";
            case "en":
                return "en";
            default:
                return DEFAULT_LANG;
        }
    }
    /*----------*/

    protected Pageable getPageRequest(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber, pageSize);
    }

    protected Pageable getPageRequest(PageNumberRequestDto pageRequestDto) {
        int pageSize = pageRequestDto == null || pageRequestDto.getPageSize() == null ? PAGE_SIZE_DEFAULT : pageRequestDto.getPageSize();
        int pageNumber = pageRequestDto == null || pageRequestDto.getPageNumber() == null ? 0 : pageRequestDto.getPageNumber() - 1;
        return PageRequest.of(pageNumber, pageSize);
    }

    protected Date getDate(int year, int month, int date, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hourOfDay, minute, second);
        return calendar.getTime();
    }

    protected DecimalFormat getDecimalFormat() {
        Locale locale = new Locale("vi", "VN");
        String pattern = "#,##0.##";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);

        return decimalFormat;
    }

    protected String translate(String lang, Object[] args, String code) {
        try {
            return messageSource.getMessage(code, args, new Locale(getValidLang(lang)));
        } catch (NoSuchMessageException e) {
            logger.warn("No translation for key '" + code + "'", e);
            return code;
        }
    }

    protected String translate(String lang, String code) {
        return translate(lang, null, code);
    }

    protected ExcelErrorMessageFactory getExcelErrorMessageFactory(String lang) {
        return new SimpleExcelErrorMessageFactory(lang, this);
    }

    protected InputStream getInputStreamFromLink(String link) throws IOException {
        if (StringUtils.isEmpty(link)) {
            throw new IOException("link null");
        }

        URL url = new URL(link);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");

        return connection.getInputStream();
    }

    protected <T extends DTO> Map<Long, T> getDtoMapById(List<T> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyMap();
        }
        Map<Long, T> map = new HashMap<>(dtos.size());
        for (T dto : dtos) {
            map.put(dto.getId(), dto);
        }

        return map;
    }

    protected <T extends PO> Map<Long, T> getDomainMapById(Collection<T> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyMap();
        }
        Map<Long, T> map = new HashMap<>(domains.size());
        for (T dto : domains) {
            map.put(dto.getId(), dto);
        }

        return map;
    }

    protected <T extends DTO> Map<Long, T> getDtoMapById(Collection<T> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyMap();
        }
        Map<Long, T> map = new HashMap<>(dtos.size());
        for (T dto : dtos) {
            map.put(dto.getId(), dto);
        }

        return map;
    }

    protected <T extends PO> boolean isContainsDomain(Collection<T> refs, T domain) {
        if (CollectionUtils.isEmpty(refs)) {
            return false;
        }
        for (PO ref : refs) {
            if (ref.getId() != null && ref.getId().equals(domain.getId())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isAdmin(UserLogin userLogin) {
        return userLogin.isRole(UserRole.ADMIN.name()) || userLogin.isRole(UserRole.SUB_ADMIN.name());
    }

    protected boolean isEcomAdmin(UserLogin userLogin) {
        return userLogin.isRole(UserRole.ECOM_ADMIN.name()) || userLogin.isRole(UserRole.ECOM_SUB_ADMIN.name());
    }

    protected boolean isPartner(UserLogin userLogin) {
        return userLogin.isRole(UserRole.PARTNER_ADMIN.name());
    }

    public static class SimpleExcelErrorMessageFactory implements ExcelErrorMessageFactory {

        protected String lang;
        protected A_Service service;

        public SimpleExcelErrorMessageFactory(String lang, A_Service service) {
            super();
            this.lang = lang;
            this.service = service;
        }

        @Override
        public String getMandatoryErrorMessage(int columnIndex) {
            return this.service.translate(lang, "mandatory.error.message");
        }

        @Override
        public String getMinLengthErrorMessage(int columnIndex, int minLength) {
            return this.service.translate(lang, new Object[] { minLength }, "min.length.error.message");
        }

        @Override
        public String getMaxLengthErrorMessage(int columnIndex, int maxLength) {
            return this.service.translate(lang, new Object[] { maxLength }, "max.length.error.message");
        }

        @Override
        public String getMinErrorMessage(int columnIndex, BigDecimal min) {
            return this.service.translate(lang, new Object[] { min.toString() }, "min.error.message");
        }

        @Override
        public String getMaxErrorMessage(int columnIndex, BigDecimal max) {
            return this.service.translate(lang, new Object[] { max.toString() }, "max.error.message");
        }

        @Override
        public String getCategoryNotFoundErrorMessage(int columnIndex, String value) {
            return this.service.translate(lang, new Object[] { value }, "category.not.found.error.message");
        }

        @Override
        public String getUsernameErrorMessage(int columnIndex, int minLength, int maxLength) {
            return this.service.translate(lang, new Object[] { minLength, maxLength }, "error.username-invalid");
        }

        @Override
        public String getLengthErrorMessage(int columnIndex, int minLength, int maxLength) {
            return this.service.translate(lang, new Object[] { minLength, maxLength }, "error.max-min-length");
        }

        @Override
        public String getPhoneErrorMessage(int columnIndex, Integer minLength, Integer maxLength) {
            return this.service.translate(lang, new Object[] { minLength, maxLength }, "error.phone-invalid");
        }

        @Override
        public String getEmailErrorMessage(int columnIndex, Integer minLength, Integer maxLength) {
            return this.service.translate(lang, new Object[] { minLength, maxLength }, "error.email-invalid");
        }

        @Override
        public String getPasswordErrorMessage(int columnIndex, Integer minLength, Integer maxLength) {
            return this.service.translate(lang, new Object[] { minLength, maxLength }, "error.password-invalid");
        }

    }

}
