package com.probro.khoded.utils

import com.probro.khoded.models.ButtonState
import com.probro.khoded.models.Images
import com.probro.khoded.models.Routes

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
        object LandingData : Home_Section(
            Routes.Home.LANDING_ROUTE,
            "Home",
            Routes.Home.SLUG,
            "${Routes.Home.SLUG}${Routes.Home.LANDING_ROUTE}"
        ) {
            val mainText: String = "Psst, a GOOD site tells a brand’s story"
            val subText: String = "Let Khoded handle all your web app, site redesign, web hosting, " +
                    "and brand + SEO needs in one place."
            val mainImage: String = Images.HomePage.mainLanding
            val underlineImage: String = Images.HomePage.mainLanding
            val ctaButton: ButtonState = ButtonState(buttonText = "Start Now!") {
                //TODO: Navigate to the schedule consultation section.
            }
        }

        object Services : Home_Section(
            id = Routes.Home.OUR_SERVICES,
            title = "Our Services",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.OUR_SERVICES}"
        ) {
            val mainImage: String = Images.HomePage.webDesign
            val underlineImage: String = Images.HomePage.webDesign
            val khodedServices = listOf(
                WebService("TAILORED WEB WIZARDRY", ""),
                WebService("TURBO-SECURE HOSTING", ""),
                WebService("ULTIMATE BRAND PLAYBOOK", "")
            )
        }

        object Design : Home_Section(
            id = Routes.Home.DESIGN,
            title = "Design",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.DESIGN}"
        ) {
            val mainText: String = "Code & design tailor-made just for your business..."
            val subText: String =
                "Khoded specializes in creating bespoke websites from the ground up, catering to a diverse clientele," +
                        " and providing comprehensive branding kits and reliable web hosting solutions to enhance our" +
                        " clients' online presence further."
            val mainImage: String = Images.HomePage.hosting
            val subImage: String = Images.HomePage.hosting
            val underlineImage: String = Images.HomePage.hosting
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
            val mainImage: String = Images.HomePage.consultation
            val subImage: String = Images.HomePage.consultation
            val consultationRequestUIModel = ConsultationRequestUIModel()
            val ctaButton: ButtonState = ButtonState(buttonText = "SCHEDULE A FREE 30 MIN CONSULTATION") {
                //TODO: Navigate to the  contact section.
            }
        }

        data class ConsultationRequestUIModel(
            val fullName: String = "Full Name",
            val email: String = "Email",
            val projectSynopsis: String = "Tell Us A Little About Your Project"
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
            val estherBio = TeamBio(
                name = "Esther Dronyi",
                position = "CEO/Co-Founder",
                image = Images.AboutPage.founderEsther,
                story = Constants.Strings.EstherFounderBio //"She cool or whateva!"
            )
            val joshBio = TeamBio(
                name = "Joshua Dronyi",
                position = "CTO/Co-Founder",
                image = Images.AboutPage.founderJosh,
                story = Constants.Strings.JoshFounderBio //"He cool or whateva!"
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
            val story: String
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
            val messaageUIModel: MessaageUIModel = MessaageUIModel()
            val contactInfoUIModel: ContactInfoUIModel = ContactInfoUIModel()
            val mainText = "Ensure your brand story never ends up in the digital abyss."
            val subText = "Hit us up to craft a site that's 404-proof"
            val mainImage: String = ""
            val ctaButton = ButtonState("") {

            }
        }

        data class MessaageUIModel(
            val fullName: String = "Full Name",
            val email: String = "Email",
            val organization: String = "Organization",
            val messageSubject: String = "What Do you need Help With",
            val message: String = "Drop Us A Message"
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
        enum class Sections(
            override val id: String,
            override val title: String,
            override val slug: String,
            override val path: String
        ) : PageSection {
            ComingSoon(
                title = "Coming soon", path = "#coming_soon", slug = "ComingSoon", id = "coming_soon"
            ),
            Testimonials(
                title = "Testimonials", path = "#tesetimonials", slug = "Testimonial", id = "testimonials"
            )
        }
    }

}

typealias WebService = Pair<String, String>
