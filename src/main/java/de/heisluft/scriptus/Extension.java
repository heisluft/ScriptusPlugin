package de.heisluft.scriptus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Extension {
  String varName = "describe";
  String format = "git-${}-%s";
  String fromDir = "";
  String fallback = "unknown";
  boolean failOnError = false;
  int hashLength = 7;
}