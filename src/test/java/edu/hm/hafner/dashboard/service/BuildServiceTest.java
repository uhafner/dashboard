package edu.hm.hafner.dashboard.service;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.dashboard.db.BuildEntityService;
import edu.hm.hafner.dashboard.db.model.BuildEntity;
import edu.hm.hafner.dashboard.db.model.WarningTypeEntity;
import edu.hm.hafner.dashboard.service.dto.Build;
import edu.hm.hafner.dashboard.service.dto.Job;
import edu.hm.hafner.dashboard.service.dto.Result;
import edu.hm.hafner.dashboard.service.table.build.BuildViewTable;
import edu.hm.hafner.echarts.BuildResult;
import io.jenkins.plugins.datatables.TableColumn;
import io.jenkins.plugins.datatables.TableModel;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test the Class {@link BuildService}.
 *
 * @author Deniz Mardin
 */
class BuildServiceTest {
    private static final String JOB_NAME = "jobName";
    private static final String SUCCESS = "Success";
    private static final int NUMBER_OF_BUILDS = 5;
    private static final int NUMBER_OF_RESULTS = 3;

    @Test
    void shouldSaveAllBuilds() {
        BuildEntityService buildEntityService = mock(BuildEntityService.class);
        BuildService buildService = new BuildService(buildEntityService);
        SoftAssertions.assertSoftly(softly -> {
            when(buildEntityService.saveAll(new ArrayList<>())).thenReturn(new ArrayList<>());
            List<Build> builds = buildService.saveAll(createJob(1), new ArrayList<>());
            softly.assertThat(builds).isEmpty();

            List<BuildEntity> buildEntities = createBuildEntities();
            when(buildEntityService.saveAll(buildEntities)).thenReturn(buildEntities);
            List<Build> buildsToSave = createBuilds();
            builds = buildService.saveAll(createJob(1), buildsToSave);
            softly.assertThat(builds).isEqualTo(buildsToSave);
        });
    }

    @Test
    void shouldGetLatestBuild() {
        BuildEntityService buildEntityService = mock(BuildEntityService.class);
        BuildService buildService = new BuildService(buildEntityService);

        SoftAssertions.assertSoftly(softly -> {
            Job jobWithoutBuilds = createJob(1);
            softly.assertThatThrownBy(() -> buildService.getLatestBuild(jobWithoutBuilds))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("No Build not found");

            Job job = createJobWithBuilds();
            Build latestBuild = buildService.getLatestBuild(job);
            Build shouldBeLatestBuild = job.getBuilds().stream().max(Comparator.comparingInt(Build::getNumber)).orElseThrow(() -> new NoSuchElementException("No Build not found"));
            softly.assertThat(latestBuild).isEqualTo(shouldBeLatestBuild);
        });
    }

    @Test
    void shouldCreateBuildResultForTool() {
        BuildEntityService buildEntityService = mock(BuildEntityService.class);
        BuildService buildService = new BuildService(buildEntityService);

        SoftAssertions.assertSoftly(softly -> {
            Job job = createJobWithBuildsAndResults();
            List<BuildResult<Build>> buildResults = buildService.createBuildResultsForTool(job, "toolName0 Warnings");
            for (int i = 0; i < buildResults.size(); i++) {
                BuildResult<Build> buildBuildResult = buildResults.get(i);
                softly.assertThat(buildBuildResult.getBuild().getDisplayName()).isEqualTo("#" + i);
                softly.assertThat(buildBuildResult.getBuild().getNumber()).isEqualTo(i);
                softly.assertThat(buildBuildResult.getBuild().getBuildTime()).isEqualTo(0);
                List<Result> results = buildBuildResult.getResult().getResults();
                for (int j = 0; j < results.size(); j++) { //TODO improve test by adding results
                    Result result = results.get(j);
                    softly.assertThat(result.getName()).isEqualTo("toolName0 Warnings");
                    softly.assertThat(result.getFixedSize()).isEqualTo(j * 10);
                    softly.assertThat(result.getNewSize()).isEqualTo(j * 10);
                    softly.assertThat(result.getTotalSize()).isEqualTo(j * 10 * 2);
                }
            }
        });
    }

    @Test
    void shouldCreateBuildViewTable() {
        BuildEntityService buildEntityService = mock(BuildEntityService.class);
        BuildService buildService = new BuildService(buildEntityService);

        SoftAssertions.assertSoftly(softly -> {
            BuildViewTable buildViewTable = buildService.createBuildViewTable();
            TableModel tableModel = buildViewTable.getTableModel("builds");
            softly.assertThat(tableModel.getId()).isEqualTo("builds");
            softly.assertThat(tableModel.getColumnsDefinition()).isEqualTo("[{  \"data\": \"buildNumber\",  \"defaultContent\": \"\"},{  \"data\": \"buildUrl\",  \"defaultContent\": \"\"}]");
            softly.assertThat(tableModel.getRows()).isEmpty();
            softly.assertThat(buildViewTable.getTableRows("builds")).isEmpty();

            List<TableColumn> tc = tableModel.getColumns();
            softly.assertThat(tc.size()).isEqualTo(2);
            softly.assertThat(tc.get(0).getHeaderLabel()).isEqualTo("Build Number");
            softly.assertThat(tc.get(0).getDefinition()).isEqualTo("{  \"data\": \"buildNumber\",  \"defaultContent\": \"\"}");
            softly.assertThat(tc.get(0).getHeaderClass()).isEqualTo("");
            softly.assertThat(tc.get(0).getWidth()).isEqualTo(1);

            softly.assertThat(tc.get(1).getHeaderLabel()).isEqualTo("Url");
            softly.assertThat(tc.get(1).getDefinition()).isEqualTo("{  \"data\": \"buildUrl\",  \"defaultContent\": \"\"}");
            softly.assertThat(tc.get(1).getHeaderClass()).isEqualTo("");
            softly.assertThat(tc.get(1).getWidth()).isEqualTo(1);
        });
    }

    private Job createJob(final int numberOfJob) {
        return new Job(
                numberOfJob,
                getJobNameForNumber(numberOfJob),
                getUrlForNumber(numberOfJob),
                SUCCESS);
    }

    private String getUrlForNumber(final int number) {
        return "http://localhost:8080/jenkins/job/" + JOB_NAME + number + "/";
    }

    private String getJobNameForNumber(final int numberOfJob) {
        return JOB_NAME + numberOfJob;
    }

    private List<BuildEntity> createBuildEntities() {
        return IntStream.range(0, NUMBER_OF_BUILDS).mapToObj(this::createBuildEntity).collect(Collectors.toList());
    }

    private BuildEntity createBuildEntity(final int numberOfBuild) {
        return new BuildEntity(
                numberOfBuild,
                numberOfBuild,
                "http://localhost:8080/jenkins/job/" + JOB_NAME + "/" + numberOfBuild + "/"
        );
    }

    private List<Build> createBuilds() {
        List<Build> builds = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_BUILDS; i++) {
            builds.add(createBuild(i));
        }
        return builds;
    }

    private Build createBuild(final int numberOfBuild) {
        return new Build(
                numberOfBuild,
                numberOfBuild,
                getUrlForBuildWithBuildNumber(numberOfBuild)
        );
    }

    private String getUrlForBuildWithBuildNumber(final int number) {
        return "http://localhost:8080/jenkins/job/" + JOB_NAME + "/" + number + "/";
    }

    private Job createJobWithBuilds() {
        Job job = createJob(1);
        List<Build> builds = createBuilds();
        builds.forEach(job::addBuild);

        return job;
    }

    private Job createJobWithBuildsAndResults() {
        Job job = createJob(1);
        List<Build> builds = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_BUILDS; i++) {
            builds.add(createBuildWithResults(i, i, JOB_NAME, NUMBER_OF_RESULTS));
        }
        builds.forEach(job::addBuild);

        return job;
    }

    private Build createBuildWithResults(final int id, final int buildNumber, final String jobName, final int numberOfResults) {
        Build build = new Build(id, buildNumber, "http://localhost:8080/jenkins/job/" + jobName + "/" + buildNumber + "/");
        for (int i = 0; i < numberOfResults; i++) {
            Result result = new Result(
                    i,
                    "toolId" + i,
                    "http://localhost:8080/jenkins/job/" + jobName + "/" + buildNumber + "/" + "toolId" + i,
                    "toolName" + i + " Warnings",
                    i * 10,
                    i * 10,
                    i * 10 * 2,
                    "INACTIVE"
            );
            result.setInfoMessages(createInfoMessage(i));
            result.setErrorMessages(createErrorMessage(i));
            for (WarningTypeEntity warningTypeEntity : WarningTypeEntity.values()) {
                switch (warningTypeEntity) {
                    case OUTSTANDING:
                        result.setOutstandingIssues(new Report());
                        break;
                    case NEW:
                        result.setNewIssues(new Report());
                        break;
                    case FIXED:
                        result.setFixedIssues(new Report());
                        break;
                }
            }
            build.addResult(result);
        }

        return build;
    }

    private List<String> createErrorMessage(final int i) {
        return Arrays.asList("Error", "Message", ": " + i);
    }

    private List<String> createInfoMessage(final int i) {
        return Arrays.asList("Info", "Message", ": " + i);
    }
}