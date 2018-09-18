import geb.spock.GebReportingSpec
import helpers.DirectoryCreator
import pages.Config
import pages.LoginPage

class UseTaskAndFileNamesCollisionTest extends GebReportingSpec
{

    def run()
    {
        when:

        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(Config.user, Config.password)

        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()
        DirectoryCreator.createPlanDirectory()
        DirectoryCreator.copyFile("googletest-demo")
        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask()

        def boostTestTaskConfiguration = tasks.selectBoostTesttask()
        boostTestTaskConfiguration.taskDescription << "myBoostTask"
        boostTestTaskConfiguration.testExecutables << "googletest-demo"
        boostTestTaskConfiguration.checkTaskNameCollision()
        boostTestTaskConfiguration.checkFileNameCollision()
        boostTestTaskConfiguration.clickSave()

        def createdPlan = configureTasksPage.clickCreateButton()

        def planBuild = createdPlan.runManualBuild()

        then:
        planBuild.waitForFailedHeader()

        when:
        planBuild.testsTabLink.click()

        then:
        planBuild.checkTextAddedToTests('googletest-demo,myBoostTask', 3)

    }
}