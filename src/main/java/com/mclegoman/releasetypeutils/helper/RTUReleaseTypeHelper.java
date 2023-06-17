package com.mclegoman.releasetypeutils.helper;

import com.mclegoman.releasetypeutils.util.RTUReleaseTypes;
import com.mclegoman.releasetypeutils.util.RTUTranslationTypes;
import com.mclegoman.releasetypeutils.util.RTUTranslations;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

public class RTUReleaseTypeHelper {
    public static Text getText(RTUReleaseTypes releaseType, RTUTranslationTypes codeType) {
        String key = ("releasetypeutils.release_type." + releaseType.toString().toLowerCase() + "." + codeType.toString().toLowerCase());
        return Text.Serializer.deserializeText(I18n.translate(key));
    }
    public static String getString(RTUReleaseTypes releaseType, RTUTranslationTypes codeType) {
        if (releaseType.equals(RTUReleaseTypes.ALPHA)) {
            if (codeType.equals(RTUTranslationTypes.CODE)) return RTUTranslations.RT_ALPHA_CODE;
            else if (codeType.equals(RTUTranslationTypes.SENTENCED)) return RTUTranslations.RT_ALPHA_SENTENCED;
            else if (codeType.equals(RTUTranslationTypes.NORMAL)) return RTUTranslations.RT_ALPHA_NORMAL;
        }
        if (releaseType.equals(RTUReleaseTypes.BETA)) {
            if (codeType.equals(RTUTranslationTypes.CODE)) return RTUTranslations.RT_BETA_CODE;
            else if (codeType.equals(RTUTranslationTypes.SENTENCED)) return RTUTranslations.RT_BETA_SENTENCED;
            else if (codeType.equals(RTUTranslationTypes.NORMAL)) return RTUTranslations.RT_BETA_NORMAL;
        }
        if (releaseType.equals(RTUReleaseTypes.RELEASE_CANDIDATE)) {
            if (codeType.equals(RTUTranslationTypes.CODE)) return RTUTranslations.RT_RELEASE_CANDIDATE_CODE;
            else if (codeType.equals(RTUTranslationTypes.SENTENCED)) return RTUTranslations.RT_RELEASE_CANDIDATE_SENTENCED;
            else if (codeType.equals(RTUTranslationTypes.NORMAL)) return RTUTranslations.RT_RELEASE_CANDIDATE_NORMAL;
        }
        if (releaseType.equals(RTUReleaseTypes.RELEASE)) {
            if (codeType.equals(RTUTranslationTypes.CODE)) return RTUTranslations.RT_RELEASE_CODE;
            else if (codeType.equals(RTUTranslationTypes.SENTENCED)) return RTUTranslations.RT_RELEASE_SENTENCED;
            else if (codeType.equals(RTUTranslationTypes.NORMAL)) return RTUTranslations.RT_RELEASE_NORMAL;
        }
        return null;
    }
    public static boolean isDevelopment(RTUReleaseTypes releaseType) {
        return !releaseType.equals(RTUReleaseTypes.RELEASE);
    }
}