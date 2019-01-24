package de.heisluft.scriptus;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gradle.api.DefaultTask;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class DescriptionTask extends DefaultTask {

  private final Extension ex;

  public DescriptionTask(ScriptusPlugin plugin) {
    ex = plugin.extension;
  }

  @TaskAction
  public void describe() {
    ExtraPropertiesExtension ext = getProject().getExtensions().getExtraProperties();
    File dir = ex.fromDir.isEmpty() ? getProject().getProjectDir() : new File(ex.fromDir);
    Consumer<String> fn = s -> ext
        .set(ex.varName, String.format(ex.format.replace("${}", getProject().getName()), s));

    try {
      Repository r = new FileRepositoryBuilder().findGitDir(dir).build();
      ObjectId id = r.resolve("HEAD");
      if(id == null) getLogger().warn("Repository contains no commits!");
      fn.accept(r.newObjectReader().abbreviate(id, ex.hashLength).name());
    } catch(RepositoryNotFoundException e) {
      throw new RuntimeException("Dir " + dir + " is not a git repo!");
    } catch(IOException e) {
      if(ex.failOnError) throw new RuntimeException("Exception reading repo!", e);
      else fn.accept(ex.fallback);
    }
  }
}
