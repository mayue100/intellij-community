package com.intellij.localvcs;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateDirectoryChange extends Change {
  private Path myPath;
  private Integer myId;
  private IdPath myAffectedEntryIdPath;

  public CreateDirectoryChange(Integer id, Path path) {
    myPath = path;
    myId = id;
  }

  public CreateDirectoryChange(Stream s) throws IOException {
    myPath = s.readPath();
  }

  @Override
  public void write(Stream s) throws IOException {
    s.writePath(myPath);
  }

  public Path getPath() {
    return myPath;
  }

  @Override
  public void applyTo(RootEntry root) {
    root.doCreateDirectory(myId, myPath);
    myAffectedEntryIdPath = root.getEntry(myPath).getIdPath();
  }

  @Override
  public void revertOn(RootEntry root) {
    root.doDelete(myId);
  }

  @Override
  protected List<IdPath> getAffectedEntryIdPaths() {
    return Arrays.asList(myAffectedEntryIdPath);
  }

  @Override
  public List<Difference> getDifferences(RootEntry r, Entry e) {
    if (!affects(e)) return Collections.emptyList();
    return Collections.singletonList(new Difference(Difference.Kind.CREATED));
  }
}
