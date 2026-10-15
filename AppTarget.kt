package com.tupaquete.aku

enum class AppTarget(
    val canonicalName: String,
    val packages: List<String>,
    val aliases: List<String>
) {
    FACEBOOK("Facebook", listOf("com.facebook.katana", "com.facebook.lite"), listOf("facebook", "face", "feis", "feisbuk", "feisbuc", "el face", "meta")),
    WHATSAPP("WhatsApp", listOf("com.whatsapp", "com.whatsapp.w4b"), listOf("whatsapp", "wasap", "guasap", "wasa", "el wasap", "wat sap", "wsp")),
    TIKTOK("TikTok", listOf("com.zhiliaoapp.musically", "com.ss.android.ugc.trill"), listOf("tiktok", "tik tok", "tiktoc", "tikto", "el tiktok", "tik")),
    CLAUDE("Claude", listOf("com.anthropic.claude"), listOf("claude", "claud", "clod", "cloud")),
    DEEPSEEK("DeepSeek", listOf("com.deepseek.chat"), listOf("deepseek", "deep seek", "dipseek", "dip sik")),
    GEMINI("Gemini", listOf("com.google.android.apps.bard"), listOf("gemini", "yemini", "gemini google", "gemini ai")),
    QWEN_STUDIO("Qwen Studio", listOf("ai.qwenlm.chat.android"), listOf("qwen", "quen", "kwen", "cuen", "qwen studio")),
    EDGE_GALLERY("Edge Gallery", listOf("com.google.ai.edge.gallery"), listOf("edge gallery", "galeria edge", "edge")),
    YOUTUBE("YouTube", listOf("com.google.android.youtube"), listOf("youtube", "yutub", "yutu", "yutú", "el youtube", "yt")),
    DEEZER("Deezer", listOf("deezer.android.app"), listOf("deezer", "diser", "dizer")),
    LARK_PLAYER("Lark Player", listOf("com.dywx.larkplayer"), listOf("lark", "lark player", "lar player", "el lark")),
    KAWAII_ANIMES("Kawaii Animes", listOf("com.darkdeveloper.kawaiianimes"), listOf("kawaii", "kawaii animes", "animes", "anime", "kaguai")),
    MOVIE_PLUS("Movie Plus", listOf("com.dvex.movp"), listOf("movie plus", "movie", "movi", "peliculas", "pelis")),
    FREEFIRE("Free Fire MAX", listOf("com.dts.freefiremax"), listOf("free fire", "freefire", "frifai", "frefire", "el free", "free")),
    BRAVE("Brave", listOf("com.brave.browser"), listOf("brave", "navegador", "browser", "internet")),
    CALCULADORA("Calculadora", listOf("com.android.calculator2"), listOf("calculadora", "calcu", "la calculadora")),
    CAMARA("Cámara", listOf("com.android.camera", "com.sec.android.app.camera"), listOf("camara", "cámara", "sacar foto", "foto")),
    CONTACTOS("Contactos", listOf("com.android.contacts"), listOf("contactos", "agenda", "los contactos")),
    GMAIL("Gmail", listOf("com.google.android.gm"), listOf("gmail", "el mail", "correo", "mail")),
    GOOGLE("Google", listOf("com.google.android.googlequicksearchbox"), listOf("google", "el google", "buscador")),
    MAPS("Maps", listOf("com.google.android.apps.maps"), listOf("maps", "mapas", "el mapa", "navegar")),
    RELOJ("Reloj", listOf("com.sec.android.app.clockpackage", "com.google.android.deskclock"), listOf("reloj", "alarma", "despertador", "clock", "hora")),
    TELEFONO("Teléfono", listOf("com.android.phone", "com.google.android.dialer"), listOf("telefono", "teléfono", "llamar", "marcar")),
    WIDGETABLE("Widgetable", listOf("com.widgetable.theme.android"), listOf("widgetable", "widget")),
    MERCADO_PAGO("Mercado Pago", listOf("com.mercadopago.wallet"), listOf("mercado pago", "mercadopago", "el mercado", "mp")),
    PERSONAL_PAY("Personal Pay", listOf("ar.com.personalpay"), listOf("personal pay", "personalpay", "personal")),
    DIDI("DiDi", listOf("com.didiglobal.passenger"), listOf("didi", "el didi")),
    MAXIM("Maxim", listOf("com.taxsee.taxsee"), listOf("maxim", "maksim", "el maxim", "taxi")),
    UBER("Uber", listOf("com.ubercab"), listOf("uber", "el uber"));

    companion object {
        fun findInText(text: String): AppTarget? {
            val lower = text.lowercase()
            return values()
                .filter { target -> target.aliases.any { lower.contains(it) } }
                .maxByOrNull { target -> target.aliases.filter { lower.contains(it) }.maxOf { it.length } }
        }
    }
}
