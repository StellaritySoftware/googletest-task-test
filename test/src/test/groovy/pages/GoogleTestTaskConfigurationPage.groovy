package pages

import commonpages.CreateNewPlanConfigureTasksPage
import geb.Page

/**
 * Created by Kateryna on 05.12.2017.
 */
class GoogleTestTaskConfigurationPage extends Page
{
    static url = "/bamboo/build/admin/create/createPlanTasks.action"
    static at = { ($("#createTask h2").text() == "GoogleTest Task configuration" || $("#updateTask h2").text() == "GoogleTest Task configuration") }

    static content = 
    {
        taskDescription { $("#createTask_userDescription") }
        taskDescriptionUpdate { $("#updateTask_userDescription") }
        disabletaskCheckbox { $("#createTask_taskDisabled") }
        disabletaskCheckboxUpdate { $("#updateTask_taskDisabled") }
        parseOnlyModeCheckbox { $("#googletestParseOnly") }
        testExecutables { $("#googletestExecutables") }
        subdirectory { $("#googletestSubdirectory") }
        taskNameCollisions { $("#googletestUseTaskName") }
        fileNameCollisions { $("#googletestUseFileName") }
        environmentVariable { $("input#googletestEnvironment") }
        timeout { $("#googletestTimeout") }
        successfulTaskCreationText { $("div.aui-message.aui-message-success").text() == "Task created successfully." }
        successfulTaskUpdatedText { $("div.aui-message.aui-message-success").text() == "Task saved successfully." }
        outpuFiles { $("#googletestOutputFiles") }
        pickOutdatedFiles { $("#googletestPickOutdatedFiles") }
        collapseSection { $("form div.collapsible-details") }
    }

    def clickSave()
    {
        js.exec(
            "var createSave = document.getElementById('createTask_save');" +
            "var updateSave = document.getElementById('updateTask_save');" +
            "createSave ? createSave.click() : updateSave.click();"
        )
        waitFor { successfulTaskCreationText || successfulTaskUpdatedText }
        browser.at CreateNewPlanConfigureTasksPage
    }

    def uncollapseAdvancedOptions()
    {
        js."document.querySelector('fieldset.collapsible-section.collapsed div.summary span.icon.icon-expand').click()"
        waitFor{collapseSection.isDisplayed()}
    }

    def enterOutputFilesName(String name)
    {
        js."document.querySelector('#googletestOutputFiles').scrollIntoView()"
        outpuFiles << name
    }

    def setEnvironmentVariable(String input) 
    {
        js."document.querySelector('input#googletestEnvironment').scrollIntoView()"
        environmentVariable << input
    }

    def checkTaskNameCollision() 
    {
        js."document.querySelector('#googletestUseTaskName').scrollIntoView"
        js."document.querySelector('#googletestUseTaskName').click()"
    }

    def checkFileNameCollision() 
    {
        js."document.querySelector('#googletestUseFileName').scrollIntoView()"
        js."document.querySelector('#googletestUseFileName').click()"
    }

    def checkPickOutdatedFiles() 
    {
        js."document.querySelector('#googletestPickOutdatedFiles').scrollIntoView()"
        pickOutdatedFiles = true
    }
 }