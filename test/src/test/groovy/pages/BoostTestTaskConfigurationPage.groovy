package pages

import geb.Page

/**
 * Created by Kateryna on 05.12.2017.
 */
class BoostTestTaskConfigurationPage extends Page{

    static url = {Config.context + "/build/admin/create/createPlanTasks.action"}
    static at = { ($("#createTask h2").text() == "Boost::Test Task configuration" ||
                $("#updateTask h2").text() == "Boost::Test Task configuration")
    }

    static content = {
        taskDescription { $("#createTask_userDescription")}
        taskDescriptionUpdate{$("#updateTask_userDescription")}
        disabletaskCheckbox {$("#createTask_taskDisabled")}
        disabletaskCheckboxUpdate {$("#updateTask_taskDisabled")}
        parseOnlyModeCheckbox{$("#boosttestParseOnly")}
        testExecutables {$("#boosttestExecutables")}
        subdirectory {$("#boosttestSubdirectory")}
        taskNameCollisions {$("#boosttestUseTaskName")}
        fileNameCollisions {$("#boosttestUseFileName")}
        environmentVariable {$("input#boosttestEnvironment")}
        timeout{$("#boosttestTimeout")}
        successfulTaskCreationText {$("div.aui-message.aui-message-success").text() == "Task created successfully."}
        successfulTaskUpdatedText {$("div.aui-message.aui-message-success").text() == "Task saved successfully."}
        outpuFiles{$("#boosttestOutputFiles")}
        pickOutdatedFiles{$("#boosttestPickOutdatedFiles")}
        collapseSection{$("form div.collapsible-details")}

    }

    def clickSave(){
        js.exec(
            "var createSave = document.getElementById('createTask_save');" +
            "var updateSave = document.getElementById('updateTask_save');" +
            "createSave ? createSave.click() : updateSave.click();"
        )
        browser.waitFor{successfulTaskCreationText || successfulTaskUpdatedText}
        browser.at CreateNewPlanConfigureTasksPage
    }

    def uncollapseAdvancedOptions(){
        js."document.querySelector('fieldset.collapsible-section.collapsed div.summary span.icon.icon-expand').click()"
        waitFor{collapseSection.isDisplayed()}
    }

    def enterOutputFilesName(String name) {
        js."document.querySelector('#boosttestOutputFiles').scrollIntoView()"
        outpuFiles << name
    }

    def checkTaskNameCollision() {
        js."document.querySelector('#boosttestUseTaskName').scrollIntoView"
        js."document.querySelector('#boosttestUseTaskName').click()"
    }

    def checkFileNameCollision() {
        js."document.querySelector('#boosttestUseFileName').scrollIntoView()"
        js."document.querySelector('#boosttestUseFileName').click()"
    }

    def checkPickOutdatedFiles() {
        js."document.querySelector('#boosttestOutputFiles').scrollIntoView()"
        pickOutdatedFiles = true
    }
 }
