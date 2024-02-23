package com.probro.khoded.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object Navigator {

    private val homeSections: List<PageSection> = listOf(
        Pages.Home_Section.Landing,
        Pages.Home_Section.Services,
        Pages.Home_Section.Design,
        Pages.Home_Section.Consultation
    )
    private val storySections = listOf(
        Pages.Story_Section.OurFounders,
        Pages.Story_Section.OurStory,
        Pages.Story_Section.JoinOurTeam
    )

    private val contactSection = listOf(
        Pages.Contact_Section.Landing
    )

    val sections: Map<KeySections, List<PageSection>> = mapOf(
        KeySections.PageRoots.home to homeSections,
        KeySections.PageRoots.story to storySections,
        KeySections.PageRoots.contact to contactSection,
        KeySections.TrafficStops.joinOurTeam to contactSection,
        KeySections.TrafficStops.consultation to contactSection,
        KeySections.TrafficStops.termsAndCondtions to contactSection,

        )

    val pageState = MutableStateFlow(NavigationState())
    fun navigateTo(section: PageSection) {
        if (section != pageState.value.currentSection) {
            pageState.update {
                val temp = it.currentSection
                it.copy(
                    currentSection = section,
                    previous = it.previous.apply {
                        if (temp != null) {
                            toMutableList().add(temp)
                        }
                    }
                )
            }
        }
    }

    fun navigateUp() {
        if (pageState.value.previous.isNotEmpty()) {
            pageState.update {
                it.copy(
                    currentSection = it.previous.last(),
                    previous = it.previous.dropLast(1)
                )
            }
        }
    }

    data class NavigationState(
        val currentSection: PageSection? = null,
        val previous: List<PageSection> = emptyList()
    )

    sealed class KeySections(
        val root: String,
        val primaryText: String
    ) {
        object PageRoots {
            data class Home(val page: String = "Home", val text: String = "Home") : KeySections(page, text)
            data class STORY(val page: String = "Story", val text: String = "Our Story") : KeySections(page, text)
            class Contact(val page: String = "Contact", val text: String = "Contact Us") : KeySections(page, text)

            val home by lazy { Home() }
            val story by lazy { STORY() }
            val contact by lazy { Contact() }
        }

        object TrafficStops {
            data class CONSULTATION(val page: String = "Index", val text: String = "Schedule a Consultation") :
                KeySections(page, text)

            data class JOIN_OUR_TEAM(val page: String = "Story", val text: String = "Join Our Team") :
                KeySections(page, text)

            data class TERMS_AND_CONDTIONS(val page: String = "Contact", val text: String = "Terms and Conditions") :
                KeySections(page, text)

            val consultation by lazy { CONSULTATION() }
            val joinOurTeam by lazy { JOIN_OUR_TEAM() }
            val termsAndCondtions by lazy { TERMS_AND_CONDTIONS() }
        }
    }
}