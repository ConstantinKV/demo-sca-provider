/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.provider

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class IndexController {

    @GetMapping(value = ["/"])
    fun showIndex(): ModelAndView = ModelAndView("index")
}
