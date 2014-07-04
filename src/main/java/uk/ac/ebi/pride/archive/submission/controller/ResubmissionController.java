package uk.ac.ebi.pride.archive.submission.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.ac.ebi.pride.archive.repo.project.service.ProjectSummary;
import uk.ac.ebi.pride.archive.repo.user.service.UserSummary;
import uk.ac.ebi.pride.archive.security.project.ProjectSecureService;
import uk.ac.ebi.pride.archive.security.user.UserSecureReadOnlyService;
import uk.ac.ebi.pride.archive.submission.model.project.ProjectDetail;
import uk.ac.ebi.pride.archive.submission.model.project.ProjectDetailList;

import java.security.Principal;
import java.util.Collection;

/**
 * @author Rui Wang
 * @version $Id$
 */
@Controller
@RequestMapping("/resubmission")
public class ResubmissionController {

    private static final Logger logger = LoggerFactory.getLogger(ResubmissionController.class);

    @Autowired
    private ProjectSecureService projectService;

    @Autowired
    private UserSecureReadOnlyService userService;

    /**
     * Request for private project accessions
     */
    @RequestMapping(value = "/projects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ProjectDetailList getUserDetail(Principal user) {
        logger.info("New -resubmission- request for user:" + user.getName());

        UserSummary userSummary = userService.findByEmail(user.getName());

        Collection<ProjectSummary> projectSummaries = projectService.findAllBySubmitterId(userSummary.getId());

        ProjectDetailList projectDetailList = new ProjectDetailList();
        for (ProjectSummary projectSummary : projectSummaries) {
            if (!projectSummary.isPublicProject()) {
                ProjectDetail projectDetail = new ProjectDetail(projectSummary.getAccession());
                projectDetailList.addProjectDetail(projectDetail);
            }
        }

        return projectDetailList;
    }

}
