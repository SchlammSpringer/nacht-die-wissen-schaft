package org.scc.samples.harrypotteronlinebookstore

import groovy.transform.Immutable
import groovy.transform.Sortable

@Immutable
@Sortable
class Book {
  int id
  String name
}
