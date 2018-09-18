import geb.spock.GebReportingSpec
import helpers.DirectoryCreator
import pages.Config
import pages.LoginPage

class UseSubdirectoryTest extends GebReportingSpec
{

    def run()
    {
        when:

        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(Config.user, Config.password)

        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()
        DirectoryCreator.createPlanDirectory("subDir")
        DirectoryCreator.copyFile("googletest-demo", "subDir")

        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask()

        def boostTestTaskConfiguration = tasks.selectBoostTesttask()
        boostTestTaskConfiguration.testExecutables << 'googletest-demo'
        boostTestTaskConfiguration.subdirectory << 'subDir'
        boostTestTaskConfiguration.clickSave()

        def createdPlan = configureTasksPage.clickCreateButton()

        def planBuild = createdPlan.runManualBuild()

        then:
        planBuild.waitForFailedHeader()

        when:
        planBuild.testsTabLink.click()

        then:
        planBuild.waitForFailedHeader()
        planBuild.checkNumberOfFailedTests('3')

    }
}