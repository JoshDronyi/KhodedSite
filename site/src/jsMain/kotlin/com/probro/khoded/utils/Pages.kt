package com.probro.khoded.utils

import com.probro.khoded.models.ButtonState
import com.probro.khoded.models.Images
import com.probro.khoded.models.Routes
import com.probro.khoded.pages.aboutSections.Founders

interface PageSection {
    val id: String
    val slug: String
    val title: String
    val path: String
}

object Pages {
    sealed class Home_Section(
        override val id: String, override val title: String, override val slug: String, override val path: String
    ) : PageSection {
        object Landing : Home_Section(
            id = Routes.Home.LANDING_ROUTE,
            title = "Home",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.LANDING_ROUTE}"
        ) {
            val mainText: String = "Psst, a GOOD site tells a brand’s story"
            val subText: String = "Let Khoded handle all your web app, site redesign, web hosting, " +
                    "and brand + SEO needs in one place."
            val mainImage: String = Images.HomePage.landing_Rocket
            val underlineImage: String = Images.Common.blueUnderline
            var ctaButton: ButtonState = ButtonState(buttonText = "Get Khoded") {
                //TODO: Navigate to the schedule consultation section.
            }
        }

        object Services : Home_Section(
            id = Routes.Home.OUR_SERVICES,
            title = "Our Services",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.OUR_SERVICES}"
        ) {
            val mainImage: String = Images.HomePage.services_ChartMaker
            val underlineImage: String = Images.Common.pinkUnderline
            val khodedServices = listOf(
                WebService("TAILORED WEB WIZARDRY", "We some wizards on the web"),
                WebService("TURBO-SECURE HOSTING", "Got that jawn toight!!"),
                WebService("ULTIMATE BRAND PLAYBOOK", " Hut hut.....Hike!!")
            )
        }

        object Design : Home_Section(
            id = Routes.Home.DESIGN,
            title = "Design",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.DESIGN}"
        ) {
            val mainText: String = "Code & design \ntailor-made just for your business..."
            val subText: String =
                "Khoded specializes in creating bespoke websites from the ground up, catering to a diverse clientele," +
                        " and providing comprehensive branding kits and reliable web hosting solutions to enhance our" +
                        " clients' online presence further."
            val mainImage: String = Images.HomePage.design_Paperplane_Computer
            val underlineImage: String = Images.Common.blackUnderline
        }

        object Consultation : Home_Section(
            id = Routes.Home.CONSULTATION,
            title = "Consultation",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.CONSULTATION}"
        ) {
            val mainText: String = "let’s chat about your \uFEFFproject!"
            val subText: String = "Crafting the perfect website is like fashioning a tale that extends beyond the " +
                    "confines of conventional storytelling. It's about creating an immersive experience that ensures" +
                    " your narrative is heard, seen, and felt in places beyond your physical reach. The right website" +
                    " becomes the storyteller you can't be in person, resonating in the vast digital landscape," +
                    " leaving a lasting impression wherever it ventures."
            val mainImage: String =
                Images.HomePage.consultation_CheckMessage

            // Images.HomePage.consultation_MessageBubble_And_Squiggle
            //            val subImage: String =
            val quotes: String = Images.HomePage.consultation_Quotes
            val ctaButton: ButtonState = ButtonState(buttonText = "SCHEDULE A FREE 30 MIN CONSULTATION") {
                //TODO: Navigate to the  contact section.
            }
        }

        data class ConsultationRequestUIModel(
            var fullName: String = Strings.namePrompt,
            var email: String = Strings.emailPrompt,
            var projectSynopsis: String = Strings.projectPrompt
        )
    }


    sealed class Story_Section(
        override val id: String,
        override val title: String,
        override val slug: String,
        override val path: String
    ) : PageSection {

        object OurStory : Story_Section(
            id = Routes.Story.OUR_STORY,
            title = "Our Story",
            slug = Routes.Story.SLUG,
            path = "${Routes.Story.SLUG}${Routes.Story.OUR_STORY}"
        ) {
            val storySections: List<StorySection> = listOf(
                StorySection(
                    title = "",
                    text = "Allow me to introduce you to Khoded, an innovative web development startup with a " +
                            "mission to transform digital presence. At Khoded, we specialize in creating bespoke " +
                            "websites from the ground up, catering to a diverse clientele. In addition, we " +
                            "provide comprehensive branding kits and reliable web hosting solutions to further" +
                            " enhance our clients' online presence."
                ),
                StorySection(
                    title = "Khoded: Our Journey and Values",
                    text = "At Khoded, our story is a testament to the powerful blend of entrepreneurial spirit," +
                            " family heritage, and a deep sense of purpose. We were inspired to create something" +
                            " meaningful and impactful, fueled by the legacy of entrepreneurship that runs " +
                            "through our veins."
                ),
                StorySection(
                    title = "Entrepreneurial Heritage:",
                    text = "Entrepreneurship is in our family's DNA. We've seen firsthand the transformative " +
                            "power of forging one's own path, seizing opportunities, and building something " +
                            "substantial from the ground up. This legacy has ignited a profound desire to" +
                            " harness our entrepreneurial spirit and turn it into something extraordinary."
                ),
                StorySection(
                    title = "Honoring Our Mother's Sacrifices:",
                    text = "Our mother, an immigrant, worked tirelessly to provide us with a better life. " +
                            "Her sacrifices serve as a constant source of motivation for us. We are committed " +
                            "to honoring her dedication by maximizing the potential she unlocked for us. Our " +
                            "ultimate aim is to grow Khoded to a level where we can give back to her and our " +
                            "family tenfold."
                ),
                StorySection(
                    title = "Using God-Given Talents:",
                    text = "We firmly believe in using the talents and abilities bestowed upon us for the " +
                            "betterment of others. Our passion lies in leveraging our God-given skills to" +
                            " create value, not just for ourselves but also for our clients, partners, and" +
                            " the communities we are privileged to serve."
                ),
                StorySection(
                    title = "Giving Back to the Community:",
                    text = "As we embark on our entrepreneurial journey, our primary goal is to give back to our" +
                            " community and society as a whole. We envision Khoded as a vehicle for positive " +
                            "impact, enabling us to support causes and initiatives that align with our core values."
                )
            )
        }


        object OurFounders : Story_Section(
            id = Routes.Story.FOUNDERS,
            title = "Our Founders",
            slug = Routes.Story.SLUG,
            path = "${Routes.Story.SLUG}${Routes.Story.FOUNDERS}"
        ) {
            val jointFoundersImage = Images.StoryPage.jointFounderImage
            
            val estherBio = TeamBio(
                name = "Esther Dronyi",
                position = "CEO/Co-Founder",
                image = Images.StoryPage.founderEsther,
                fullStory = Strings.EstherFounderBio, //"She cool or whateva!"
                founderType = Founders.CEO,
                title = Strings.EstherTitle,
                desc = Strings.EstherDesc,
                shortDesc = Strings.EstherShortDesc
            )
            val joshBio = TeamBio(
                name = "Joshua Dronyi",
                position = "CTO/Co-Founder",
                image = Images.StoryPage.founderJosh,
                fullStory = Strings.JoshFounderBio, //"He cool or whateva!"
                founderType = Founders.CTO,
                title = Strings.JoshTitle,
                desc = Strings.JoshDesc,
                shortDesc = Strings.JoshShortDesc
            )
        }


        object JoinOurTeam : Story_Section(
            id = Routes.Story.JOIN_OUR_TEAM,
            title = "Join Our Team",
            slug = Routes.Story.SLUG,
            path = "${Routes.Story.SLUG}${Routes.Story.JOIN_OUR_TEAM}"
        ) {

            val mainImage: String = ""
            val positions: List<JobPosition> = listOf(
                JobPosition(
                    positionTitle = "UX/UI Developer",
                    positionDesc = "Responsible for creating visually appealing and user-friendly products."
                ),
                JobPosition(
                    positionTitle = "Graphic Designer",
                    positionDesc = "Maintains clean and efficient code for software applications and systems."
                ),
                JobPosition(
                    positionTitle = "Business/Finance Human",
                    positionDesc = "Analyzing and interpreting large datasets to identify trends."
                ),
                JobPosition(
                    positionTitle = "FullStack Developer",
                    positionDesc = "Maintains clean and efficient code for software applications and systems."
                ),
                JobPosition(
                    positionTitle = "Project Manager",
                    positionDesc = "Oversees projects from start to finish, ensuring timely completion."
                ),
                JobPosition(
                    positionTitle = "Marketing",
                    positionDesc = "Developing strategies to drive business recognition."
                )
            )
        }

        data class TeamBio(
            val name: String,
            val position: String,
            val image: String,
            val fullStory: String,
            val shortDesc: String,
            val desc: String,
            val title: String,
            val founderType: Founders
        )

        data class JobPosition(
            val positionTitle: String,
            val positionDesc: String
        )

        data class StorySection(
            val title: String,
            val text: String
        )
    }


    sealed class Contact_Section(
        override val id: String, override val title: String, override val slug: String, override val path: String
    ) : PageSection {
        object Landing : Contact_Section(
            id = Routes.Contact.LANDING,
            title = "Contact Us",
            slug = Routes.Contact.SLUG,
            path = "${Routes.Contact.SLUG}${Routes.Contact.LANDING}"
        ) {
            val contactInfoUIModel: ContactInfoUIModel = ContactInfoUIModel()
            val mainText = "Ensure your brand story never ends up in the digital abyss."
            val subText = "Hit us up to craft a site that's 404-proof"
            val mainImage: String = Images.ContactPage.planet404
            val ctaButton = ButtonState("Get Khoded") {

            }
        }

        data class MessageUIModel(
            var fullName: String = "",
            var email: String = "",
            var organization: String = "",
            var messageSubject: String = "",
            var message: String = ""
        )

        data class ContactInfoUIModel(
            val address: String = "2389 main St. STE 100 Glastonbury, CT,06033",
            val phone: String = "833-454-6333",
            val email: String = "admin@khoded.com"
        )
    }


    sealed class StylingClass(val name: String) {
        object Testimonials : StylingClass("testimonials")
    }

    object Misc {
        sealed class Sections(
            override val id: String,
            override val title: String,
            override val slug: String,
            override val path: String
        ) : PageSection {
            object TermsAndConditions : Sections(
                id = Routes.Misc.TERMS_AND_CONDTIONS,
                title = "TERMS AND CONDITIONS",
                slug = Routes.Misc.TERMS_AND_CONDTIONS,
                path = Routes.Misc.TERMS_AND_CONDTIONS
            )
        }
    }

}

typealias WebService = Pair<String, String>
