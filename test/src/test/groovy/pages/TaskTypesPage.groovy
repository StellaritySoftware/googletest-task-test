package pages

import geb.Page

/**
 * Created by Kateryna on 03.12.2017.
 */
class TaskTypesPage extends Page{

    static url =  Config.context + "/build/admin/edit/addTask.action"
    static at = { $("#task-types-dialog h2.dialog-title").text() == "Task types"}

    static content =
    {
       boostTest{ $("div#task-types-dialog a", 0, title:"Boost::Test Task") }

    }

    def selectBoostTesttask(){
        boostTest.click()
        browser.at BoostTestTaskConfigurationPage
    }
}
