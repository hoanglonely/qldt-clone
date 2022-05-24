package com.mb.lab.banks.apigateway.config;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mb.lab.banks.utils.security.UserAuthenticationToken;
import com.mb.lab.banks.utils.security.UserLogin;

@Component
public class UserAuthenticationConverter extends DefaultUserAuthenticationConverter {

	public Authentication extractAuthentication(Map<String, ?> map) {
		if (map.containsKey(USERNAME)) {
			Object principal = map.get(USERNAME);
			Collection<? extends GrantedAuthority> authorities = getAuthorities(map);

			Long userId = getLongValue(principal);

			Optional<String> role = authorities == null ? Optional.empty()
					: authorities.stream().map(authority -> authority.getAuthority().substring(5))
							.filter(authority -> !"USER".equals(authority)).findFirst();

			String username = (String) map.get("username");

			UserLogin userLogin = new UserLogin(role.get(), userId, username);

			UserAuthenticationToken auth = new UserAuthenticationToken(principal, "N/A", authorities);
			auth.setUserLogin(userLogin);

			return auth;
		}
		return null;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
		if (!map.containsKey(AUTHORITIES)) {
			return null;
		}
		Object authorities = map.get(AUTHORITIES);
		if (authorities instanceof String) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
		}
		if (authorities instanceof Collection) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList(
					StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
		}
		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
	}

	private static Long getLongValue(Object obj) {
		if (obj instanceof Long) {
			return (Long) obj;
		}
		if (obj instanceof Number) {
			return ((Number) obj).longValue();
		}
		if (obj instanceof String) {
			return Long.valueOf(obj.toString());
		}
		return null;
	}
}