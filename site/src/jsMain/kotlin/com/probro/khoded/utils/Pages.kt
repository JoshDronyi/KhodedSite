package com.probro.khoded.utils

import com.probro.khoded.models.ButtonState
import com.probro.khoded.models.Images
import com.probro.khoded.models.Routes
import com.probro.khoded.models.Testimonial

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
            val mainText: String = "The right website will tell your story in the places and spaces you can't"
            val subText: String = "Here you can provide a brief description about your project."
            val image: String = Images.HomePage.mainLanding
            val startButton: ButtonState = ButtonState(buttonText = "Start Now!") {
                //TODO: Navigate to the schedule consultation section.
            }
            val learnMoreButton: ButtonState = ButtonState(buttonText = "Learn More") {
                //TODO: Navigate to the about page
            }
        }

        object WebDesign : Home_Section(
            id = Routes.Home.WEB_DESIGN,
            title = "Web Design",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.WEB_DESIGN}"
        ) {
            val mainText: String = "Customized web design for your needs"
            val subText: String = "Lorem ipsum bayyybyyyyyyyyy"
            val image: String = Images.HomePage.webDesign
            val learnMoreButton: ButtonState = ButtonState(buttonText = "Learn More") {
                //TODO: Navigate to the about page
            }
        }

        object Hosting : Home_Section(
            id = Routes.Home.HOSTING,
            title = "Hosting",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.HOSTING}"
        ) {
            val mainText: String = "Fast & Secure Web Hosting Services"
            val subText: String = "Lorem ipsum bayyybyyyyyyyyy"
            val image: String = Images.HomePage.hosting
            val learnMoreButton: ButtonState = ButtonState(buttonText = "Learn More") {
                //TODO: Navigate to the about page
            }
        }

        object Branding : Home_Section(
            id = Routes.Home.BRANDING,
            title = "Branding",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.BRANDING}"
        ) {
            val mainText: String = "Intentional & Strategic Brand Kits"
            val subText: String =
                "LOOOOOOwwwRAAAAYYYYMMMMMMMMMM EEEEEPPPPPPPSOOOOOOOOOOOOUUUUUUUMMMMMMMMM BAAAAIIIIIIIIEEEEEEEYYYYBBUUUUUUHHHHHHYYYYYYYYYYYYYYY"
            val image: String = Images.HomePage.branding
            val tryItButton: ButtonState = ButtonState(buttonText = "Try it Now") {
                //TODO: ASK WHERE THIS IS TO NAVIGATE TO .
            }
        }

        object Consultation : Home_Section(
            id = Routes.Home.CONSULTATION,
            title = "Consultation",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.CONSULTATION}"
        ) {
            val mainText: String = "Schedule a FREE Consultation"
            val subText: String = "lOREM iPSUM bayyyyybyyyyyyyyyyyyyyyyyyyy"
            val image: String = Images.HomePage.consultation
            val steps: List<Pair<String, String>> = listOf(
                Pair("Step 1", "Fill out our client intake form + some lorem and include even ipsum"),
                Pair(
                    "Step 2", "Take our 30 minutes complimentary consultation to find out" +
                            " which services work best for your needs.."
                ),
                Pair("Step 3", "Embark on the journey to your unique web tool")
            )
            val startButton: ButtonState = ButtonState(buttonText = "Start Now!") {
                //TODO: Navigate to the  contact section.
            }
        }

        object Testimonials : Home_Section(
            id = Routes.Home.TESTIMONIALS,
            title = "Testimonials",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.TESTIMONIALS}"
        ) {
            val mainText: String = "What our clients say about us"

            // val image: String = ""
            val testimonialList: List<Testimonial> = listOf(
                Testimonial(),
                Testimonial(),
                Testimonial()
            )
        }

        object GET_STARTED : Home_Section(
            id = Routes.Home.GET_STARTED,
            title = "Get Started",
            slug = Routes.Home.SLUG,
            path = "${Routes.Home.SLUG}${Routes.Home.GET_STARTED}"
        ) {
            val mainText: String = "Start your web journey with Khoded today"
            val subText: String = "Lorem Ipsum BAAAAIIIIEEEEYYYBBBBBBEEEEEEEEEE"
            val image: String = Images.HomePage.getStarted
            val startButton: ButtonState = ButtonState(buttonText = "Get Started") {
                //TODO: Navigate to the  contact section.
            }
        }
    }


    sealed class About_Section(
        override val id: String,
        override val title: String,
        override val slug: String,
        override val path: String
    ) : PageSection {
        object Landing : About_Section(
            id = Routes.About.LANDING,
            title = "about",
            slug = Routes.About.SLUG,
            path = "${Routes.About.SLUG}${Routes.About.LANDING}"
        ) {
            val mainText: String = "Meet Our team"
            val subText: String = "Passionate Experts"
            val image: String = Images.AboutPage.mainLanding
        }

        object Team : About_Section(
            id = Routes.About.TEAM,
            title = "Team",
            slug = Routes.About.SLUG,
            path = "${Routes.About.SLUG}${Routes.About.TEAM}"
        ) {
            val mainText = "Meet the Team"
            val teambios: List<TeamBio> = listOf(
                TeamBio(
                    name = "Esther Dronyi",
                    position = "Ceo/Co-Founder",
                    image = Images.AboutPage.founderEsther,
                    story = Constants.Strings.LOREM_IPSUM_PARAGRAPH //"She cool or whateva!"
                ),
                TeamBio(
                    name = "Joshua Dronyi",
                    position = "CTO/Co-Founder",
                    image = Images.AboutPage.founderJosh,
                    story = Constants.Strings.LOREM_IPSUM_PARAGRAPH //"He cool or whateva!"
                )
            )
        }

        object Story : About_Section(
            id = Routes.About.STORY,
            title = "Story",
            slug = Routes.About.SLUG,
            path = "${Routes.About.SLUG}${Routes.About.STORY}"
        ) {
            val mainText = "Our Story"
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

        object Separator : About_Section(
            id = Routes.About.SEPARATOR,
            title = "about",
            slug = Routes.About.SLUG,
            path = "${Routes.About.SLUG}${Routes.About.SEPARATOR}"
        )

        object Opportunities : About_Section(
            id = Routes.About.OPPORTUNITIES,
            title = "New Opportunites",
            slug = Routes.About.SLUG,
            path = "${Routes.About.SLUG}${Routes.About.OPPORTUNITIES}"
        ) {

            val mainText: String = "New Opportunities"
            val subText: String = "Join our skilled team."
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

    sealed class Services_Section(
        override val id: String,
        override val title: String,
        override val slug: String,
        override val path: String
    ) : PageSection {
        object Landing : Services_Section(
            id = Routes.Services.LANDING,
            title = "Services",
            slug = Routes.Services.SLUG,
            path = "${Routes.Services.SLUG}${Routes.Services.LANDING}"
        ) {
            val mainText = "Services"
            val subText = "Find the perfect plan"
        }

        object ServiceBreakdown : Services_Section(
            id = Routes.Services.SERVICE_BREAKDOWN,
            title = "Service Breakdown",
            slug = Routes.Services.SLUG,
            path = "${Routes.Services.SLUG}${Routes.Services.SERVICE_BREAKDOWN}"
        ) {
            val mainText: String = "Compare Features"
        }

        object FAQ : Services_Section(
            id = Routes.Services.FAQ,
            title = "FAQ",
            slug = Routes.Services.SLUG,
            path = "${Routes.Services.SLUG}${Routes.Services.FAQ}"
        ) {
            val mainText: String = "Frequently Asked Questions"
            val image: String = Images.ServicePage.faq
            val questionList: List<FAQQuestion> = emptyList()
        }


        object GetStarted : Services_Section(
            id = Routes.Services.GET_STARTED,
            title = "Get Started",
            slug = Routes.Services.SLUG,
            path = "${Routes.Services.SLUG}${Routes.Services.GET_STARTED}"
        ) {
            val mainText: String = "Get Started with Khoded today"
            val subText: String = "Start optimizing your processes today"
            val signUpButtonState: ButtonState = ButtonState(
                buttonText = "Sign Up Now",
                onButtonClick = {}
            )
            val image: String = Images.ServicePage.getStarted
        }

        data class FAQQuestion(
            val question: String = "Are you the best?",
            val explanation: String = "Short answer, yes. Long answer, yyyyyyyeeeeeeeeeessssssss"
        )
    }

    sealed class Contact_Section(
        override val id: String, override val title: String, override val slug: String, override val path: String
    ) : PageSection {
        data class MessaageUIModel(
            val name: String = "",
            val email: String = "",
            val organization: String = "",
            val message: String = ""
        )

        object Landing : Contact_Section(
            id = Routes.Contact.LANDING,
            title = "Contact Us",
            slug = Routes.Contact.SLUG,
            path = "${Routes.Contact.SLUG}${Routes.Contact.LANDING}"
        ) {
            val message: MessaageUIModel = MessaageUIModel()
            val thankYouMessage = "Thank you for your message ${message.name}! We will get back to you as soon as" +
                    " possible to address this."
        }

        object IntakeForm : Contact_Section(
            id = Routes.Contact.INTAKE_FORM,
            title = "Client Request Form",
            slug = Routes.Contact.SLUG,
            path = "${Routes.Contact.SLUG}${Routes.Contact.INTAKE_FORM}"
        ) {
            //TODO: GET DATA CLASS FOR INTAKE FORM RESPONSES CALLED CLIENT REQUEST.
        }

        object ContactInfo : Contact_Section(
            id = Routes.Contact.CONTACT_US,
            title = "Contact Information",
            slug = Routes.Contact.SLUG,
            path = "${Routes.Contact.SLUG}${Routes.Contact.CONTACT_US}"
        ) {
            val uiModel: ContactInfoUIModel = ContactInfoUIModel()
        }

        data class ContactInfoUIModel(
            val address: String = "2389 main St. STE 100",
            val city: String = "Glastonbury, CT",
            val zip: String = "06033",
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
