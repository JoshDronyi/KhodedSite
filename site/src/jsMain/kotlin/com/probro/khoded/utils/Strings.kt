package com.probro.khoded.utils

object Strings {
    val emailPrompt: String = "Email"
    val namePrompt: String = "Full Name"
    val projectPrompt: String = "Tell Us A Little About Your Project"


    val visionStatement: String =
        "Our vision at Brand Crafters is to be the driving force behind business transformations, where each brand " +
                "we touch becomes a symbol of authenticity, innovation, and connection. We envision a future where " +
                "each collaboration echoes the trust and belief clients have in us, transforming their brainchild into a reality that resonates in hearts and minds. With every project, we aspire to be the catalyst that turns visions into realities,fostering a world where trust and brand loyalty flourish naturally."
    val missionStatement: String =
        "At Brand Crafters, our mission is to empower businesses by meticulously crafting their brand identities. We specialize in guiding business owners through the process of forming, developing, and expanding their unique brand visions. Rooted in a deep commitment to our clients, our goal is to cultivate a space where every client entrusts us with their brand's essence."
    val visionStatementTitle: String = "Vision Statement:"
    val missionStatementTitle: String = "Mission Statement:"
    val servicesTitle: String = "Services Breakdown"
    val formTitle: String = "FORM:"
    val developTitle: String = "DEVELOP:"
    val ExpandTitle: String = "EXPAND:"
    val formStringList = listOf(
        "Brand Statements",
        "Brand Identity",
        "Brand Pull (target audience)",
        "Brand Essence (how do you want to be percieved)"
    )
    val developStringList = listOf(
        "Branding Kit (colors, fonts, logos)",
        "materials/templates (flyers, brochures, presentations)",
        "online presence (website design & dev)"
    )
    val expandStringList = listOf(
        "Brand adoption (internally)",
        "Organizational structure & identity",
        "Brand Strength"
    )


    val consultationThanksMessage =
        "Thanks for considering Khoded for your project. We shall get back to you as soon as possible."

    val EstherFounderBio = "Hey there! I'm a soon-to-be grown-up adult who's absolutely in love with this wild" +
            " adventure called life. My days are filled with a little bit of everything, but if I had to pick my" +
            " top loves, it'd be traveling, spending quality time with the incredible people in my life, and " +
            "cozying up under a blanket with a delicious drink and a captivating book, movie, or show - pure " +
            "bliss, right?"
    val JoshFounderBio =
        "Hello world! I'm just a regular tech and music enthusiast who's all about my family and creating a path" +
                " for those behind me that is easier to walk than the one I had. In any and every way I can, I am" +
                " committed to making the journey a little smoother for my future family,  my loved ones, and the" +
                " next generation of developers and musicians alike\n"

    val LOREM_IPSUM_PARAGRAPH =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et" +
                " dolore magna aliqua. Eleifend mi in nulla posuere. Ut aliquam purus sit amet luctus venenatis." +
                " Posuere ac ut consequat semper viverra nam libero justo. In nibh mauris cursus mattis molestie" +
                " a. Lorem dolor sed viverra ipsum nunc aliquet bibendum enim. Mattis aliquam faucibus purus in" +
                " massa tempor nec. Massa sapien faucibus et molestie ac feugiat. Etiam sit amet nisl purus. Id " +
                "venenatis a condimentum vitae sapien."


    val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
}
