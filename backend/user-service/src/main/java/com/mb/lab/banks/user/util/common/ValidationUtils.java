package com.mb.lab.banks.user.util.common;

import org.springframework.util.Assert;

import com.mb.lab.banks.utils.common.StringUtils;
import com.mb.lab.banks.utils.exception.BusinessAssert;

public class ValidationUtils {

	public static final String getValidText(String text, boolean mandatory, int min, int max) {
		return getValidText(text, mandatory, min, max, false);
	}

	public static final String getValidText(String text, boolean mandatory, int min, int max, boolean autocrop) {
		Assert.isTrue(min > 0 && max >= min, "min > 0 && max >= min");

		if (mandatory) {
			BusinessAssert.isTrue(!StringUtils.isEmpty(text));
		}

		if (!StringUtils.isEmpty(text)) {
			text = text.trim();
			BusinessAssert.isTrue(text.length() >= min);

			if (autocrop) {
				if (text.length() > max) {
					return text.substring(0, max);
				} else {
					return text;
				}
			} else {
				BusinessAssert.isTrue(text.length() <= max);
			}

			return text;
		}

		return null;
	}

}
