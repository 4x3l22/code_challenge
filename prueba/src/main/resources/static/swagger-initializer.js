window.onload = function() {
    fetch('/swagger-token')  // <- llamamos al backend para traer el token
        .then(res => res.ok ? res.text() : null)
        .then(token => {
            const ui = SwaggerUIBundle({
                url: "/v3/api-docs",  // <- importante: esto NO debe fallar
                dom_id: '#swagger-ui',
                deepLinking: true,
                presets: [
                    SwaggerUIBundle.presets.apis,
                    SwaggerUIStandalonePreset
                ],
                layout: "BaseLayout"
            });

            if (token) {
                ui.initOAuth({});
                ui.getConfigs().authActions.authorize({
                    BearerAuth: {
                        name: "BearerAuth",
                        schema: {
                            type: "http",
                            in: "header",
                            scheme: "bearer",
                            bearerFormat: "JWT"
                        },
                        value: "Bearer " + token
                    }
                });
            }
        });
};
