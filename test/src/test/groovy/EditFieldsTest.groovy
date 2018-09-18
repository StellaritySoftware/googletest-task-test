import geb.spock.GebReportingSpec
import pages.Config
import pages.LoginPage

class EditFieldsTest extends GebReportingSpec
{

    def run()
    {
        when:

        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(Config.user, Config.password)

        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()
        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask()

        def boostTestTaskConfiguration = tasks.selectBoostTesttask()
        boostTestTaskConfiguration.taskDescription << "my_task"
        boostTestTaskConfiguration.disabletaskCheckbox = true
        boostTestTaskConfiguration.testExecutables << "my_test,testFile_Test"
        boostTestTaskConfiguration.subdirectory << "subDir"
        boostTestTaskConfiguration.uncollapseAdvancedOptions()
        boostTestTaskConfiguration.setEnvironmentVariable("JAVA_OPTS=-Xmx256m -Xms128m")
        boostTestTaskConfiguration.taskNameCollisions = true
        boostTestTaskConfiguration.fileNameCollisions = true
        boostTestTaskConfiguration.timeout = "5"

        boostTestTaskConfiguration.clickSave()

        configureTasksPage.editBoostTestTask()
// FIRST CHECK
        then:
        boostTestTaskConfiguration.taskDescriptionUpdate.value() == "my_task"
        boostTestTaskConfiguration.disabletaskCheckboxUpdate == "true"
        boostTestTaskConfiguration.testExecutables.value() == "my_test,testFile_Test"
        boostTestTaskConfiguration.subdirectory.value() == "subDir"

        when:
        boostTestTaskConfiguration.uncollapseAdvancedOptions()

        then:
        boostTestTaskConfiguration.environmentVariable.value() == "JAVA_OPTS=-Xmx256m -Xms128m"
        boostTestTaskConfiguration.taskNameCollisions.value() == "true"
        boostTestTaskConfiguration.fileNameCollisions.value() == "true"
        boostTestTaskConfiguration.timeout.value() == "5"
// END FIRST CHECK

// SECOND CHECK

        when:
        configureTasksPage.editBoostTestTask()

        boostTestTaskConfiguration.taskDescriptionUpdate = "second_task"
        boostTestTaskConfiguration.disabletaskCheckboxUpdate = false
        boostTestTaskConfiguration.parseOnlyModeCheckbox = true
        boostTestTaskConfiguration.enterOutputFilesName("testXml.xml")
        boostTestTaskConfiguration.subdirectory = ""
        boostTestTaskConfiguration.taskNameCollisions = false
        boostTestTaskConfiguration.fileNameCollisions = false
        boostTestTaskConfiguration.checkPickOutdatedFiles()

        boostTestTaskConfiguration.clickSave()

        configureTasksPage.editBoostTestTask()

        then:
        boostTestTaskConfiguration.taskDescriptionUpdate.value() == "second_task"
        boostTestTaskConfiguration.disabletaskCheckboxUpdate.value() == null
        boostTestTaskConfiguration.parseOnlyModeCheckbox.value() == "true"
        boostTestTaskConfiguration.subdirectory.value() == ""

        when:
        boostTestTaskConfiguration.uncollapseAdvancedOptions()

        then:
        boostTestTaskConfiguration.outpuFiles.value() == "testXml.xml"
        boostTestTaskConfiguration.taskNameCollisions.value() == null
        boostTestTaskConfiguration.fileNameCollisions.value() == null
        boostTestTaskConfiguration.pickOutdatedFiles.value() == "true"

    }
}