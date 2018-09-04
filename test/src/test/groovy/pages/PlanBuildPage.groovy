package pages



import geb.Page
import org.openqa.selenium.By


/**
 * Created by Kateryna on 09.12.2017.
 */
class PlanBuildPage extends Page {

    static at = {$(By.id("breadcrumb:${Config.projKey}-${Config.planKey}"))}
    static content = {
        successfulHeader{$("div#sr-build.status-ribbon-status.Successful")}
        failedHeader{$("div#sr-build.status-ribbon-status.Failed")}
        buttonActions{$("button.aui-button.aui-dropdown2-trigger span.aui-icon.aui-icon-small.aui-iconfont-configure")}
        configurePlanLink{$(By.id("editBuild:${Config.projKey}-${Config.planKey}"))}
        defaultJobLink{$(By.id("viewJob_${Config.projKey}-${Config.planKey}-JOB1"))}
        failedLabel{$(By.cssSelector("div.test-summary li.new-failures a"))}
        compilationWarining{$(By.cssSelector("div.result-summary-tab div.aui-message.warning p strong"))}
        testsTabLink{$(By.id("tests:${Config.projKey}-${Config.planKey}-1"))}
    }

    def waitForSuccessfulHeader() {
        waitFor {successfulHeader.isDisplayed()}
    }

    def waitForFailedHeader() {
        waitFor {failedHeader.isDisplayed()}
    }

    def clickEditPalnLink(){
        buttonActions.click()
        configurePlanLink.click()
    }

    def clickDefaultJobLink(){
        waitFor {defaultJobLink.isDisplayed()}
        defaultJobLink.click()
    }

    def checkNumberOfFailedTests(CharSequence number){
        failedLabel.text().contains(number)
    }

    def waitForCompilationWarning() {
        waitFor {compilationWarining.isDisplayed()}
        compilationWarining.text() == "No failed tests found, a possible compilation error occurred."
    }

    def checkTextAddedToTests(String fileName, Integer expectedTestsCount) {
        return driver.findElements(By.xpath("//table[@id='new-failed-tests']//td/span[contains(text(), '{${fileName}}')]")).size() == expectedTestsCount
    }
 }
