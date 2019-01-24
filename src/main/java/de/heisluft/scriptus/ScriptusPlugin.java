package de.heisluft.scriptus;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
public class ScriptusPlugin implements Plugin<Project> {

  Extension extension;

  @Override
  public void apply(Project target) {
    extension = target.getExtensions().create("scriptus", Extension.class);
    target.getTasks().create("describe", DescriptionTask.class, extension);
  }
}