package com.probro.khoded.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object Navigator {

    private val homeSections: List<PageSection> = listOf(
        Pages.Home_Section.LandingData,
        Pages.Home_Section.WebDesign,
        Pages.Home_Section.Hosting,
        Pages.Home_Section.Branding,
        Pages.Home_Section.Consultation,
        Pages.Home_Section.GET_STARTED,
    )
    private val aboutSection = listOf(
        Pages.About_Section.Landing,
        Pages.About_Section.Team,
        Pages.About_Section.Story,
        Pages.About_Section.Separator,
        Pages.About_Section.Opportunities
    )
    private val servicesSection = listOf(
        Pages.Services_Section.Landing,
        Pages.Services_Section.GetStarted,
        Pages.Services_Section.ServiceBreakdown
    )
    private val contactSection = listOf(
        Pages.Contact_Section.Landing,
        Pages.Contact_Section.ContactInfo,
        Pages.Contact_Section.IntakeForm
    )

    val sections: Map<PageRoot, List<PageSection>> = mapOf(
        PageRoot.Home to homeSections,
        PageRoot.About to aboutSection,
        PageRoot.Services to servicesSection,
        PageRoot.Contact to contactSection,
    )

    val pageState = MutableStateFlow(NavigationState())
    fun navigateTo(section: PageSection) {
        if (section != pageState.value.currentSection) {
            pageState.update {
                val temp = it.currentSection
                it.copy(currentSection = section, previous = it.previous.apply {
                    toMutableList().add(temp)
                })
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
        val currentSection: PageSection = Pages.Home_Section.LandingData,
        val previous: List<PageSection> = emptyList()
    )

    enum class PageRoot(
        val primaryText: String,
        val alternateText: String
    ) {
        Home("Home", "Schedule A Consultation"),
        About("About", "About"),
        Services("Services", "Services"),
        Contact("Contact", "Contact Us")
    }
}