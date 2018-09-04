package pages

import geb.Page

class DashboardPage extends Page
{
    static url = "/bamboo/start.action"
    static at = { $("#content h1").text() == "Build Dashboard" }

    static content =
    {
        buttonCreate { $(" #createPlanLink") }
        createPlan(to: CreateNewPlanConfigurePlanPage) { $("#createNewPlan") }
        administrationButton{$("#system_admin_menu .aui-dropdown2-trigger")}
        userManagementOption {$("[href='/bamboo/admin/user/viewUsers.action']")}
    }

    def openUserManagementPage()
    {
        waitFor {administrationButton.click()}
        waitFor {userManagementOption.click()}
        browser.at UserManagementPage
    }

    def createNewPlan()
    {
        buttonCreate.click()
        createPlan.click()
        browser.at CreateNewPlanConfigurePlanPage
    }
}