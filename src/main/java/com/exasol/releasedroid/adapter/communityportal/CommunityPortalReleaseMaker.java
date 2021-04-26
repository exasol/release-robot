package com.exasol.releasedroid.adapter.communityportal;

import java.util.logging.Logger;

import com.exasol.releasedroid.usecases.exception.ReleaseException;
import com.exasol.releasedroid.usecases.release.ReleaseMaker;
import com.exasol.releasedroid.usecases.repository.ReleaseLetter;
import com.exasol.releasedroid.usecases.repository.Repository;

/**
 * This class is responsible for releasing on Exasol Community Portal.
 */
public class CommunityPortalReleaseMaker implements ReleaseMaker {
    private static final Logger LOGGER = Logger.getLogger(CommunityPortalReleaseMaker.class.getName());
    private final CommunityPortalGateway communityPortalGateway;

    /**
     * Create a new instance of {@link CommunityPortalReleaseMaker}.
     * 
     * @param communityPortalGateway instance of {@link CommunityPortalGateway}
     */
    public CommunityPortalReleaseMaker(final CommunityPortalGateway communityPortalGateway) {
        this.communityPortalGateway = communityPortalGateway;
    }

    @Override
    public void makeRelease(final Repository repository) throws ReleaseException {
        LOGGER.fine("Creating a draft of the release announcement on the Exasol Community Portal.");
        try {
            final var communityPost = getCommunityPost(repository);
            this.communityPortalGateway.sendDraftPost(communityPost);
        } catch (final CommunityPortalException exception) {
            throw new ReleaseException(exception);
        }
    }

    private CommunityPost getCommunityPost(final Repository repository) {
        final String version = repository.getVersion();
        final var communityPortalTemplate = getCommunityPortalTemplate(repository);
        final var releaseLetter = repository.getReleaseLetter(version);
        final String header = communityPortalTemplate.getProjectName() + " " + version;
        final String gitHubReleaseLink = "https://github.com/" + repository.getName() + "/releases/tag/" + version;
        final String body = renderBody(communityPortalTemplate, releaseLetter, header, gitHubReleaseLink);
        return CommunityPost.builder() //
                .boardId("ProductNews") //
                .header(header + " released") //
                .tags(communityPortalTemplate.getTags()) //
                .body(body) //
                .build();
    }

    private CommunityPortalTemplate getCommunityPortalTemplate(final Repository repository) {
        final var json = repository.getSingleFileContentAsString("community_portal_post_template.json");
        return CommunityPortalTemplateJsonParser.parse(json);
    }

    private String renderBody(final CommunityPortalTemplate communityPortalTemplate, final ReleaseLetter releaseLetter,
            final String header, final String gitHubReleaseLink) {
        final var projectDescription = communityPortalTemplate.getProjectDescription();
        final String changesDescription = releaseLetter.getSummary().orElseThrow();
        final var communityPostRenderer = new CommunityPostRenderer();
        return communityPostRenderer.renderCommunityPostBody(header, projectDescription, changesDescription,
                gitHubReleaseLink);
    }
}