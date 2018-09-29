import commonpages.LoginPage
import configuration.CommonConfig
import geb.spock.GebReportingSpec
import pages.GoogleTestTaskConfigurationPage
import pages.TaskTypesPage

class EditFieldsTest extends GebReportingSpec
{

    def run()
    {
        when:

        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(CommonConfig.user, CommonConfig.password)

        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()
        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask(TaskTypesPage)

        def googleTestTaskConfiguration = tasks.selectGoogleTestTask()
        googleTestTaskConfiguration.taskDescription << "my_task"
        googleTestTaskConfiguration.disabletaskCheckbox = true
        googleTestTaskConfiguration.testExecutables << "my_test,testFile_Test"
        googleTestTaskConfiguration.subdirectory << "subDir"
        googleTestTaskConfiguration.uncollapseAdvancedOptions()
        googleTestTaskConfiguration.setEnvironmentVariable("JAVA_OPTS=-Xmx256m -Xms128m")
        googleTestTaskConfiguration.taskNameCollisions = true
        googleTestTaskConfiguration.fileNameCollisions = true
        googleTestTaskConfiguration.timeout = "5"

        googleTestTaskConfiguration.clickSave()

        configureTasksPage.editTask(GoogleTestTaskConfigurationPage)
// FIRST CHECK
        then:
        googleTestTaskConfiguration.taskDescriptionUpdate.value() == "my_task"
        googleTestTaskConfiguration.disabletaskCheckboxUpdate == "true"
        googleTestTaskConfiguration.testExecutables.value() == "my_test,testFile_Test"
        googleTestTaskConfiguration.subdirectory.value() == "subDir"

        when:
        googleTestTaskConfiguration.uncollapseAdvancedOptions()

        then:
        googleTestTaskConfiguration.environmentVariable.value() == "JAVA_OPTS=-Xmx256m -Xms128m"
        googleTestTaskConfiguration.taskNameCollisions.value() == "true"
        googleTestTaskConfiguration.fileNameCollisions.value() == "true"
        googleTestTaskConfiguration.timeout.value() == "5"
// END FIRST CHECK

// SECOND CHECK

        when:
        configureTasksPage.editTask(GoogleTestTaskConfigurationPage)

        googleTestTaskConfiguration.taskDescriptionUpdate = "second_task"
        googleTestTaskConfiguration.disabletaskCheckboxUpdate = false
        googleTestTaskConfiguration.parseOnlyModeCheckbox = true
        googleTestTaskConfiguration.enterOutputFilesName("testXml.xml")
        googleTestTaskConfiguration.subdirectory = ""
        googleTestTaskConfiguration.taskNameCollisions = false
        googleTestTaskConfiguration.fileNameCollisions = false
        googleTestTaskConfiguration.checkPickOutdatedFiles()

        googleTestTaskConfiguration.clickSave()

        configureTasksPage.editTask(GoogleTestTaskConfigurationPage)

        then:
        googleTestTaskConfiguration.taskDescriptionUpdate.value() == "second_task"
        googleTestTaskConfiguration.disabletaskCheckboxUpdate.value() == null
        googleTestTaskConfiguration.parseOnlyModeCheckbox.value() == "true"
        googleTestTaskConfiguration.subdirectory.value() == ""

        when:
        googleTestTaskConfiguration.uncollapseAdvancedOptions()

        then:
        googleTestTaskConfiguration.outpuFiles.value() == "testXml.xml"
        googleTestTaskConfiguration.taskNameCollisions.value() == null
        googleTestTaskConfiguration.fileNameCollisions.value() == null
        googleTestTaskConfiguration.pickOutdatedFiles.value() == "true"

    }
}