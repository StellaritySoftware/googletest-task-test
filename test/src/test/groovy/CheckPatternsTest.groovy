import geb.spock.GebReportingSpec
import helpers.DirectoryCreator
import pages.Config
import pages.LoginPage

class CheckPatternsTest extends GebReportingSpec
{

    def run()
    {
        when:

        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(Config.user, Config.password)

        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()
        DirectoryCreator.createPlanDirectory()
        DirectoryCreator.copyFile("my_test")
        DirectoryCreator.copyFile("libboost_unit_test_framework.so.1.58.0")
        DirectoryCreator.copyFile("testFile_Test")
        DirectoryCreator.copyFile("t_st")
        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask()

        def boostTestTaskConfiguration = tasks.selectBoostTesttask()
        boostTestTaskConfiguration.testExecutables << "*test*"
        boostTestTaskConfiguration.checkFileNameCollision()
        boostTestTaskConfiguration.clickSave()

        def createdPlan = configureTasksPage.clickCreateButton()

        def planBuild = createdPlan.runManualBuild()

        then:
        planBuild.waitForFailedHeader()

        when:
        planBuild.testsTabLink.click()

        then:
        planBuild.checkTextAddedToTests('my_test', 5)
        planBuild.checkTextAddedToTests('testFile_Test', 5)
        planBuild.checkTextAddedToTests('t_st', 0)

    }
}