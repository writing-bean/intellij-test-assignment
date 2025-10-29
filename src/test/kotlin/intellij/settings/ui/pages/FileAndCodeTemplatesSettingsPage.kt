package intellij.settings.pages

import com.intellij.driver.sdk.ui.UiText
import com.intellij.driver.sdk.ui.components.UiComponent
import com.intellij.driver.sdk.ui.components.UiComponent.Companion.waitFound
import com.intellij.driver.sdk.ui.ui
import com.intellij.driver.sdk.ui.xQuery
import kotlin.time.Duration.Companion.seconds

class FileAndCodeTemplatesSettingsPage(
    private val pageComponent: UiComponent
) {

    enum class Tab(val label: String) {
        INCLUDES("Includes"),
        SETTINGS("Code"),
        OTHER("Other"),
        FILES("Files")
    }

    enum class Checkbox(val label: String) {
        ENABLE_LIVE_TEMPLATES("Enable Live Templates"),
        REFORMAT_ACCORDING_TO_STYLE("Reformat according to style")
    }

    // This is a more generic component that should be encapsulated in a higher scope (e.g., SettingsPage)
    // I put it here only to limit the scope of the test assignment
    enum class ControlButton(val label: String) {
        CANCEL("Cancel"),
        APPLY("Apply"),
        OK("OK")
    }

    fun clickOnTabByName(tab: Tab): FileAndCodeTemplatesSettingsPage {
        pageComponent.apply {
            val targetTab = x("//div[@class='TabLabel' and @accessiblename='${tab.label}']")
            targetTab.waitFound()
            targetTab.click()
        }
        return this
    }

    fun clickOnCheckboxByName(checkbox: Checkbox): FileAndCodeTemplatesSettingsPage {
        pageComponent.apply {
            val targetCheckbox = x(xQuery { byAccessibleName(checkbox.label) })
            targetCheckbox.click()
        }
        return this
    }

    fun clickOnControlButtonByName(controlButton: ControlButton): FileAndCodeTemplatesSettingsPage {
        pageComponent.apply {
            val targetButton = driver.ui.x(xQuery { byAccessibleName(controlButton.label) })
            targetButton.doubleClick()
        }
        return this
    }


    fun clickOnSchemeComboSelector(): FileAndCodeTemplatesSettingsPage {
        pageComponent.apply {
            val schemeSelector = x(xQuery { byClass("SchemesCombo") })
            schemeSelector.click()
        }
        return this
    }

    // Should also be a part of a broader generic Settings scope
    fun clickOnRevertChangesLink(): FileAndCodeTemplatesSettingsPage {
        pageComponent.apply {
            val revertChangesLink = driver.ui.x(xQuery { byAccessibleName("Revert changes") })
            revertChangesLink.click()
        }
        return this
    }

    fun typeTextInEditorField(text: String): FileAndCodeTemplatesSettingsPage {
        pageComponent.apply {
            val templateEditorField = x( xQuery { byAccessibleName("Editor") })
            templateEditorField.click()
            templateEditorField.keyboard {typeText(text)}
        }
        return this
    }

    // Should also be a part of a broader generic Settings scope
    fun getSchemesListAsText(): List<String> {
        pageComponent.apply {
            val schemesList = driver.ui.x(xQuery { byClass("MyList") })
            return schemesList.getAllTexts().map{ it.text }
        }
    }

    fun getDescriptionText(): List<UiText> {
        pageComponent.apply {
            val descriptionTextPane =
                x("//div[@class='JLabel' and @visible_text='Description:']/following-sibling::div[@class='JBScrollPane']//div[@class='JBViewport']//div[@class='JEditorPane']")
            return descriptionTextPane.getAllTexts()
        }
    }

    fun isRevertChangesLinkPresent(expectedToBe: Boolean = true): Boolean {
        pageComponent.apply {
            val revertChangesLink = driver.ui.x(xQuery { byAccessibleName("Revert changes") })
            if (expectedToBe) {
                revertChangesLink.waitFound(3.seconds)
            } else {
                revertChangesLink.waitNotFound(3.seconds)
            }
            return revertChangesLink.present()
        }
    }

    fun isTabActive(tab: Tab): Boolean {
        pageComponent.apply {
            val targetTabAsActiveOne = x("//div[@class='JBEditorTabs' and @accessiblename='${tab.label}']")
            targetTabAsActiveOne.waitFound()
            return targetTabAsActiveOne.present()
        }
    }

}