import commonpages.LoginPage
import configuration.CommonConfig
import geb.spock.GebReportingSpec

import helpers.DirectoryCreator
import pages.TaskTypesPage

class OnlyTestFileNameEnteredTest extends GebReportingSpec
{
    def run()
    {
        when:

        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(CommonConfig.user, CommonConfig.password)

        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()

        DirectoryCreator.createPlanDirectory()
        DirectoryCreator.copyFile("googletest-demo")

        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask(TaskTypesPage)

        def googleTestTaskConfiguration = tasks.selectGoogleTestTask()
        googleTestTaskConfiguration.testExecutables << "googletest-demo"
        googleTestTaskConfiguration.clickSave()

        def createdPlan = configureTasksPage.clickCreateButton()

        def planBuild = createdPlan.runManualBuild()

        then:

        planBuild.waitForFailedHeader()
        planBuild.checkNumberOfFailedTests('3')
    }
}