import geb.spock.GebReportingSpec
import helpers.DirectoryCreator
import configuration.CommonConfig
import commonpages.LoginPage
import pages.TaskTypesPage


class ParseXmlTest extends GebReportingSpec
{
    def run()
    {
        when:

        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(CommonConfig.user, CommonConfig.password)

        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()

        DirectoryCreator.createPlanDirectory()
        DirectoryCreator.copyFile("report.xml")

        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask(TaskTypesPage)

        def googleTestTaskConfiguration = tasks.selectGoogleTestTask()
        googleTestTaskConfiguration.parseOnlyModeCheckbox = true
        googleTestTaskConfiguration.enterOutputFilesName("report.xml")
        googleTestTaskConfiguration.uncollapseAdvancedOptions()
        googleTestTaskConfiguration.checkPickOutdatedFiles()

        googleTestTaskConfiguration.clickSave()
        
        configureTasksPage.markEnablePlanCheckbox()

        def createdPlan = configureTasksPage.clickCreateButton()

        def planBuild = createdPlan.runManualBuild()

        planBuild.testsTabLink.click()

        then:

        planBuild.waitForFailedHeader()
        planBuild.checkNumberOfFailedTests('3')
    }
}