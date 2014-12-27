/*******************************************************************************
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.google.gwt.eclipse.wtp.maven;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.wtp.WTPProjectConfigurator;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

import java.util.List;

/**
 * A {@link WTPProjectConfigurator} that adds GWT facets.
 */
@SuppressWarnings("restriction")
public class GwtProjectConfigurator extends WTPProjectConfigurator {

  @Override
  public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor)
      throws CoreException {
    // Given pom.xml configuration
    Model pom = request.getMavenProject().getModel();

    // When having GWT plugin
    if (isGwtProject(pom)) {
      IProject eclipseProject = request.getProject();
      IFacetedProject facetedProject = ProjectFacetsManager.create(eclipseProject);

      // Then add GWT facet
      new GwtFacetManager().addGwtFacet(pom, facetedProject, monitor);
    }
  }

  @Override
  public AbstractBuildParticipant getBuildParticipant(IMavenProjectFacade projectFacade,
      MojoExecution execution, IPluginExecutionMetadata executionMetadata) {
    GwtMavenPlugin.logInfo("GwtProjectConfigurator.getBuildParticipant invoked");

    return super.getBuildParticipant(projectFacade, execution, executionMetadata);
  }

  /**
   * TODO ****** only turn on with facet flag????? *****
   *
   * TODO consider using Thomas project too?
   */
  private static boolean isGwtProject(Model pom) {
    List<Plugin> plugins = pom.getBuild().getPlugins();
    for (Plugin plugin : plugins) {
      if (Constants.GWT_MAVEN_PLUGIN_ARTIFACT_ID.equals(plugin.getArtifactId())) {
        return true;
      }
    }

    return false;
  }

}
