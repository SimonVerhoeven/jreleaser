/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2023 The JReleaser authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jreleaser.cli;

import org.jreleaser.model.internal.JReleaserContext;
import org.jreleaser.workflow.Workflows;
import picocli.CommandLine;

/**
 * @author Andres Almiray
 * @since 0.3.0
 */
@CommandLine.Command(name = "upload")
public class Upload extends AbstractPlatformAwareModelCommand<Main> {
    @CommandLine.Option(names = {"--dry-run"})
    Boolean dryrun;

    @CommandLine.ArgGroup
    Composite composite;

    static class Composite {
        @CommandLine.ArgGroup(exclusive = false, order = 1,
            headingKey = "include.filter.header")
        Include include;

        @CommandLine.ArgGroup(exclusive = false, order = 2,
            headingKey = "exclude.filter.header")
        Exclude exclude;

        String[] includedUploaderTypes() {
            return null != include ? include.includedUploaderTypes : null;
        }

        String[] includedUploaderNames() {
            return null != include ? include.includedUploaderNames : null;
        }

        String[] includedDistributions() {
            return null != include ? include.includedDistributions : null;
        }

        String[] excludedUploaderTypes() {
            return null != exclude ? exclude.excludedUploaderTypes : null;
        }

        String[] excludedUploaderNames() {
            return null != exclude ? exclude.excludedUploaderNames : null;
        }

        String[] excludedDistributions() {
            return null != exclude ? exclude.excludedDistributions : null;
        }
    }

    static class Include {
        @CommandLine.Option(names = {"-u", "--uploader"},
            paramLabel = "<uploader>")
        String[] includedUploaderTypes;

        @CommandLine.Option(names = {"-un", "--uploader-name"},
            paramLabel = "<name>")
        String[] includedUploaderNames;

        @CommandLine.Option(names = {"-d", "--distribution"},
            paramLabel = "<distribution>")
        String[] includedDistributions;
    }

    static class Exclude {
        @CommandLine.Option(names = {"-xu", "--exclude-uploader"},
            paramLabel = "<uploader>")
        String[] excludedUploaderTypes;

        @CommandLine.Option(names = {"-xun", "--exclude-uploader-name"},
            paramLabel = "<name>")
        String[] excludedUploaderNames;

        @CommandLine.Option(names = {"-xd", "--exclude-distribution"},
            paramLabel = "<distribution>")
        String[] excludedDistributions;
    }

    @Override
    protected void doExecute(JReleaserContext context) {
        if (null != composite) {
            context.setIncludedUploaderTypes(collectEntries(composite.includedUploaderTypes(), true));
            context.setIncludedUploaderNames(collectEntries(composite.includedUploaderNames()));
            context.setIncludedDistributions(collectEntries(composite.includedDistributions()));
            context.setExcludedUploaderTypes(collectEntries(composite.excludedUploaderTypes(), true));
            context.setExcludedUploaderNames(collectEntries(composite.excludedUploaderNames()));
            context.setExcludedDistributions(collectEntries(composite.excludedDistributions()));
        }
        Workflows.upload(context).execute();
    }

    @Override
    protected Boolean dryrun() {
        return dryrun;
    }
}
