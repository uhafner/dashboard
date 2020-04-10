package edu.hm.hafner.warningsngui.ui;

import edu.hm.hafner.warningsngui.service.UiService;
import edu.hm.hafner.warningsngui.service.dto.Build;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Provides the Controller to display the error and info messages.
 *
 * @author Deniz Mardin
 */
@Controller
public class MessageController {
    private final UiService uiService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Creates a new instance of {@link MessageController}.
     *
     * @param uiService the service for interactions with the ui
     */
    @Autowired
    public MessageController(UiService uiService) {
        this.uiService = uiService;
    }

    /**
     * Displays error and info messages.
     *
     * @param jobName     the name of the job
     * @param buildNumber the build number
     * @param toolId      the used tool id (e.g checkstyle)
     * @param model       the model with the messages
     * @return the message page
     */
    @RequestMapping(path={"/job/{jobName}/build/{buildNumber}/{toolId}/messages"}, method= RequestMethod.GET)
    public String getInfoAndErrorMessages(
            @PathVariable("jobName") String jobName,
            @PathVariable("buildNumber") Integer buildNumber,
            @PathVariable("toolId") String toolId,
            final Model model) {
        logger.info("getInfoAndErrorMessages is called");
        Build build = uiService.getBuildWithBuildNumberFromJob(jobName, buildNumber);
        model.addAttribute("infoMessages", uiService.getInfoMessagesFromResultWithToolId(build, toolId));
        model.addAttribute("errorMessages", uiService.getErrorMessagesFromResultWithToolId(build, toolId));
        model.addAttribute("toolId", toolId);
        model.addAttribute("toolIdWithMessage", toolId + " / messages");

        return "message";
    }
}