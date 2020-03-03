package com.gfs.cpp.proxy.clm;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class UpdateCppUrlRequestDataBuilder {

    static final String LAUNCH_CPP_TXT = "Launch CPP";
    static final String LAUNCH_FURTHERANCE_TXT = "Launch Furtherance";
    static final String LAUNCH_CPP_AMENDMENT_TXT = "Launch CPP For Amendment";

    static final String ICMCPP_LINK_KEY = "ICMCPPLink";
    static final String ICMCPP_AMENDMENT_LINK_KEY = "ICMCPPAmendmentLink";
    static final String ICMCPP_FURTHERANCE_LINK_KEY = "ICMCPPFurtheranceLink";

    static final String CPP_RESOURCE = "pricinginformation";
    static final String CPP_FURTHERANCE_RESOURCE = "furtheranceinformation";

    private static final String CONTRACT_TYPE = "contractType";
    private static final String AGREEMENT_ID = "agreementId";
    static final String DEFAULT_LINK = "https://homeplate.gfs.com";
    static final String DEFAULT_TEXT = "Do Not Click (Refresh Page)";

    @Value("${icm.update.api.cppAppUrl}")
    private String cppAppUrl;

    public String buildUpdateUrlRequestBody(String agreementMetaDataString, String agreementId, String contractType, boolean isAmendment) {

        if (isAmendment) {
            String buildCppLink = buildCppLink(agreementId, contractType, LAUNCH_CPP_AMENDMENT_TXT, CPP_RESOURCE);
            return updateUrlToRequestBody(agreementMetaDataString, ICMCPP_AMENDMENT_LINK_KEY, buildCppLink, false);
        }

        String buildCppLink = buildCppLink(agreementId, contractType, LAUNCH_CPP_TXT, CPP_RESOURCE);
        boolean resetFurtheranceLink = shouldResetFurtheranceLink(agreementMetaDataString);
        return updateUrlToRequestBody(agreementMetaDataString, ICMCPP_LINK_KEY, buildCppLink, resetFurtheranceLink);
    }

    private boolean shouldResetFurtheranceLink(String agreementMetaDataString) {
        Gson gson = new Gson();
        JsonElement agreementJsonElement = gson.fromJson(agreementMetaDataString, JsonElement.class);
        JsonObject agreementJsonObject = agreementJsonElement.getAsJsonObject();

        JsonElement furtheranceLinkObject = agreementJsonObject.get(ICMCPP_FURTHERANCE_LINK_KEY);

        if (furtheranceLinkObject == null) {
            return false;
        }

        return StringUtils.containsIgnoreCase(furtheranceLinkObject.getAsString(), CPP_FURTHERANCE_RESOURCE);
    }

    public String buildFurtheranceUrlRequestBody(String agreementMetaDataString, String agreementId, String contractType) {
        String buildCppLink = buildCppLink(agreementId, contractType, LAUNCH_FURTHERANCE_TXT, CPP_FURTHERANCE_RESOURCE);
        return updateUrlToRequestBody(agreementMetaDataString, ICMCPP_FURTHERANCE_LINK_KEY, buildCppLink, false);
    }

    private String updateUrlToRequestBody(String agreementMetaDataString, String linkPropertyName, String urlValue, boolean resetFurtheranceLink) {
        Gson gson = new Gson();
        JsonElement fromJson = gson.fromJson(agreementMetaDataString, JsonElement.class);
        JsonObject asJsonObject = fromJson.getAsJsonObject();

        asJsonObject.addProperty(linkPropertyName, urlValue);

        if (resetFurtheranceLink) {
            asJsonObject.addProperty(ICMCPP_FURTHERANCE_LINK_KEY, buildDefaultFurtheranceUrl());
        }

        return gson.toJson(gson.toJson(asJsonObject));
    }

    private String buildDefaultFurtheranceUrl() {
        return "<a target='_blank' class='urlStyle' href='" + DEFAULT_LINK + "'>" + DEFAULT_TEXT + "</a>";
    }

    String buildCppLink(String agreementId, String contractType, String launchUrlTxt, String resource) {
        String cppUrl = cppAppUrl + resource + "?" + AGREEMENT_ID + "=" + agreementId + "&" + CONTRACT_TYPE + "=" + contractType;
        return "<a target='_blank' class='urlStyle' href='" + cppUrl + "'>" + launchUrlTxt + "</a>";
    }

}
