plugins {
    id("com.gradleup.nmcp.settings").version("1.6.2")
}

rootProject.name = "rt-key-client"

val centralPortalUsername = providers.environmentVariable("CENTRAL_PORTAL_USERNAME")
    .filter { it.isNotBlank() }
    .orElse(providers.gradleProperty("centralPortalUsername"))
    .filter { it.isNotBlank() }
    .orNull
val centralPortalPassword = providers.environmentVariable("CENTRAL_PORTAL_PASSWORD")
    .filter { it.isNotBlank() }
    .orElse(providers.gradleProperty("centralPortalPassword"))
    .filter { it.isNotBlank() }
    .orNull
val projectVersion = providers.gradleProperty("version").filter { it.isNotBlank() }.orNull

nmcpSettings {
    centralPortal {
        if (centralPortalUsername != null) {
            username.set(centralPortalUsername)
        }
        if (centralPortalPassword != null) {
            password.set(centralPortalPassword)
        }
        publishingType = "USER_MANAGED"
        if (projectVersion != null) {
            publicationName.set("rt-key-client:$projectVersion")
        }
    }
}
