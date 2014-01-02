package org.fenixedu.bennu.core.rest.json;

import java.util.Locale;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.json.JsonBuilder;
import org.fenixedu.commons.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AuthenticatedUserViewer implements JsonViewer<Void> {
    @Override
    public JsonElement view(Void nothing, JsonBuilder context) {
        User user = Authenticate.getUser();
        JsonObject json;
        if (user != null) {
            json = context.view(user).getAsJsonObject();
            json.add("groups", context.view(Authenticate.accessibleGroups()));
        } else {
            json = new JsonObject();
        }
        final Locale currentLocale = I18N.getLocale();
        json.add("locale", context.view(currentLocale));
        json.add("locales", context.view(CoreConfiguration.supportedLocales()));
        return json;
    }

}
