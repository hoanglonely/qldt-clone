package com.mb.lab.banks.auth.service.internal;

import com.mb.lab.banks.auth.dto.internal.TokenDto;

public interface InternalAccessTokenService {

    public TokenDto createMerchantAccessToken(Long clientId);

    public TokenDto restoreMerchantAccessToken(Long clientId, String accessToken);

    public void deleteMerchantAccessToken(Long clientId);

    public TokenDto createAppAccessToken(String clientId);

    public TokenDto restoreAppAccessToken(String clientId, String accessToken);

    public void deleteAppAccessToken(String clientId);

}
