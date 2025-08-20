package com.probro.khoded.utils

/**
 * Centralized string resources and content management for the Khoded application.
 *
 * This object serves as the single source of truth for all text content,
 * form labels, marketing copy, and user-facing strings throughout the application.
 * Following internationalization best practices, this centralization enables
 * easy content updates, translation support, and consistent messaging.
 *
 * The strings are organized into logical groups:
 * - Form labels and prompts
 * - Company mission and vision statements  
 * - Service descriptions and breakdowns
 * - Founder biographies and personal content
 * - Utility strings (lorem ipsum, regex patterns)
 *
 * @since 1.0.0
 * @see com.probro.khoded.components.composables.FormComposables for form usage
 * @see com.probro.khoded.pages.aboutSections for biography content usage
 */
object Strings {
    // Form field labels and prompts
    
    /** Email input field placeholder text. */
    val emailPrompt: String = "Email"
    
    /** Full name input field placeholder text. */
    val namePrompt: String = "Full Name"
    
    /** Project description textarea placeholder text. */
    val projectPrompt: String = "Tell Us A Little About Your Project"


    
    // Company mission and vision content
    
    /** 
     * Company vision statement - the long-term aspirational goals and impact. 
     * Used in about sections and company overview materials.
     */
    val visionStatement: String =
        "Our vision at Brand Crafters is to be the driving force behind business transformations, where each brand " +
                "we touch becomes a symbol of authenticity, innovation, and connection. We envision a future where " +
                "each collaboration echoes the trust and belief clients have in us, transforming their brainchild into a reality that resonates in hearts and minds. With every project, we aspire to be the catalyst that turns visions into realities,fostering a world where trust and brand loyalty flourish naturally."
    /** 
     * Company mission statement - the core purpose and approach to business. 
     * Used in about sections and company overview materials.
     */
    val missionStatement: String =
        "At Brand Crafters, our mission is to empower businesses by meticulously crafting their brand identities. We specialize in guiding business owners through the process of forming, developing, and expanding their unique brand visions. Rooted in a deep commitment to our clients, our goal is to cultivate a space where every client entrusts us with their brand's essence."
    
    // Section titles and headings
    
    /** Title for the company vision statement section. */
    val visionStatementTitle: String = "Vision Statement:"
    
    /** Title for the company mission statement section. */
    val missionStatementTitle: String = "Mission Statement:"
    
    /** Title for the services breakdown section. */
    val servicesTitle: String = "Services Breakdown"
    
    /** Title for the brand formation services section. */
    val formTitle: String = "FORM:"
    
    /** Title for the brand development services section. */
    val developTitle: String = "DEVELOP:"
    
    /** Title for the brand expansion services section. */
    val ExpandTitle: String = "EXPAND:"
    
    // Service offering descriptions organized by category
    
    /** 
     * List of brand formation services offered.
     * Used in services breakdown sections to detail FORM category offerings.
     */
    val formStringList = listOf(
        "Brand Statements",
        "Brand Identity",
        "Brand Pull (target audience)",
        "Brand Essence (how do you want to be percieved)"
    )
    
    /** 
     * List of brand development services offered.
     * Used in services breakdown sections to detail DEVELOP category offerings.
     */
    val developStringList = listOf(
        "Branding Kit (colors, fonts, logos)",
        "materials/templates (flyers, brochures, presentations)",
        "online presence (website design & dev)"
    )
    
    /** 
     * List of brand expansion services offered.
     * Used in services breakdown sections to detail EXPAND category offerings.
     */
    val expandStringList = listOf(
        "Brand adoption (internally)",
        "Organizational structure & identity",
        "Brand Strength"
    )


    
    // User interaction messages
    
    /** 
     * Thank you message displayed after consultation form submission.
     * Provides confirmation and sets expectations for response time.
     */
    val consultationThanksMessage =
        "Thanks for considering Khoded for your project. We shall get back to you as soon as possible."

    
    // Founder biography content - Esther
    
    /** 
     * Esther's profile headline - concise introduction for team sections.
     */
    val EstherTitle = "Dive into Life's Adventures with a Biomedical Engineer and Music Aficionado"
    
    /** 
     * Esther's short biography for compact team displays and previews.
     */
    val EstherShortDesc =
        "Meet a spirited biomedical engineer who delights in travel, family time, and the magic of music. " +
                "With a knack for crafting medical devices, this individual is on a mission to spread positivity " +
                "and leave a lasting mark. Join the fun and embrace every moment!"
    /** 
     * Esther's full biography for detailed team pages and about sections.
     * Includes personal interests, professional background, and philosophy.
     */
    val EstherDesc =
        "Hey there, friend! Ready to dive into life's adventures with me? I'm all about savoring the simple joys," +
                " whether it's exploring new places, sharing laughs with loved ones, or getting lost in the rhythm" +
                " of a good tune.\n" +
                "\n" +
                "As a biomedical engineer, I've poured my heart into creating tools that make a difference in " +
                "people's lives. It's incredible how even the smallest actions can leave a lasting impact!\n" +
                "\n" +
                "But hey, it's not all about work. Let's make sure to fill our days with laughter, love, and " +
                "plenty of good vibes. Together, we'll craft a legacy of positivity and joy. So grab your sense " +
                "of adventure, and let's make some memories!"

    /** 
     * Esther's comprehensive founder biography for company story sections.
     * Detailed professional background and personal mission statement.
     */
    val EstherFounderBio = "Hey there! I'm a soon-to-be grown-up adult who's absolutely in love with this wild" +
            " adventure called life. My days are filled with a little bit of everything, but if I had to pick my" +
            " top loves, it'd be traveling, spending quality time with the incredible people in my life, and " +
            "cozying up under a blanket with a delicious drink and a captivating book, movie, or show - pure " +
            "bliss, right?" +
            "I've got a serious soft spot for music, too. You may catch me belting out showtunes, flying through " +
            "runs under my breath or just losing myself in the magic of a good melody. There's just something about" +
            " music that speaks to my soul!\n" +
            "\n" +
            "By training, I have a bachelors and masters in biomedical engineering and have spent the last 6 years" +
            " working in the medical device space on the manufacture, design, product development & quality risk" +
            " management of minimally invasive surgical tools. I've developed such a tender respect for how deeply" +
            " one person's day to day can affect another person's lifelong hopes or dreams. It's been an honor to " +
            "serve that diligently and a blessing to know that the sentiment holds true far beyond medical " +
            "devices. \n" +
            "\n" +
            "Above all else? I want to look back one day and know that even in the slight, often overlooked ways," +
            " I had a purposeful life. So, join me for the ride and trust us with your vision for the future. " +
            "Let's make some memories, create some magic, and leave the world a little brighter than we found it." +
            " Cheers to living life to the fullest!"

    
    // Founder biography content - Josh
    
    /** 
     * Josh's profile headline - concise introduction for team sections.
     */
    val JoshTitle = " Meet a Dedicated Tech and Music Enthusiast Building a Brighter Future"
    
    /** 
     * Josh's short biography for compact team displays and previews.
     */
    val JoshShortDesc =
        "Meet a seasoned tech and music enthusiast with a decade of experience in web and android development. " +
                "Committed to teaching and crafting passion projects, this individual is dedicated to family, " +
                "community, and leaving a lasting legacy.\n"
    /** 
     * Josh's full biography for detailed team pages and about sections.
     * Includes personal interests, professional background, and philosophy.
     */
    val JoshDesc =
        "Hey there! I'm a tech and music enthusiast dedicated to family, community, and paving an easier path for " +
                "future generations. With a decade of experience in web and android development, I'm passionate about" +
                " teaching others and building rewarding passion projects. When I'm not coding, you'll find me " +
                "strumming away on my guitar, immersed in the Philadelphia underground music scene. Join me in " +
                "crafting a beautiful legacy through code, music, and cherished family moments. Cheers!"

    /** 
     * Josh's comprehensive founder biography for company story sections.
     * Detailed professional background and personal mission statement.
     */
    val JoshFounderBio =
        "Hello world! I'm just a regular tech and music enthusiast who's all about my family and creating a path" +
                " for those behind me that is easier to walk than the one I had. In any and every way I can, I am" +
                " committed to making the journey a little smoother for my future family,  my loved ones, and the" +
                " next generation of developers and musicians alike\n" +
                "I've been diving into web and android development for a good 10 years now, and for the past three " +
                "years, I've had a blast teaching others the ropes. I've also got a few passion project apps that " +
                "I've built, which have been pretty rewarding to work on. When my hands arent tapping away at my " +
                "keyboard, I tend to let them loose on a different instrument. You can find me with my guitar " +
                "strapped around my shoulders as a singer-songwriter whos been grafted into the Philadelphia" +
                " underground scene for the last decade. \n" +
                "\n" +
                "I'm dedicated to the things I put my hands to and get real satisfaction from turning the wildest" +
                " of imaginings into reality... as much as code & music is able to, of course. Above all, I cherish" +
                " the moments with my family - laughing, passing around stories, and making memories that we'll be" +
                " retelling long into our golden years. Cheers to crafting a beautiful legacy!"

    
    // Utility strings and patterns
    
    /** 
     * Standard Lorem Ipsum paragraph for placeholder content during development.
     * Used for testing layouts and content structures before final copy is ready.
     */
    val LOREM_IPSUM_PARAGRAPH =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et" +
                " dolore magna aliqua. Eleifend mi in nulla posuere. Ut aliquam purus sit amet luctus venenatis." +
                " Posuere ac ut consequat semper viverra nam libero justo. In nibh mauris cursus mattis molestie" +
                " a. Lorem dolor sed viverra ipsum nunc aliquet bibendum enim. Mattis aliquam faucibus purus in" +
                " massa tempor nec. Massa sapien faucibus et molestie ac feugiat. Etiam sit amet nisl purus. Id " +
                "venenatis a condimentum vitae sapien."


    
    /** 
     * Regular expression pattern for basic email validation.
     * 
     * Note: This is a simplified pattern for basic client-side validation.
     * For production applications, use a more comprehensive email validation
     * library and always validate on the server side as well.
     * 
     * Pattern breakdown:
     * - ^[A-Za-z0-9+_.-]+ : Username part (letters, numbers, +, _, ., -)
     * - @ : Required @ symbol
     * - [A-Za-z0-9.-]+ : Domain name part
     * - .[a-z0-9.-] : Top-level domain part
     */
    val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[a-z0-9.-]"
}
