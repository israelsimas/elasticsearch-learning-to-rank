package com.o19s.es.ltr;

import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertNoFailures;
import static org.hamcrest.Matchers.equalTo;

import com.o19s.es.TestExpressionsPlugin;
import com.o19s.es.explore.ExplorerQueryBuilder;
import com.o19s.es.termstat.TermStatQueryBuilder;
import java.util.Arrays;
import java.util.Collection;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.test.ESIntegTestCase;

/*
   These tests mostly verify that shard vs collection stat counting is working as expected.
*/
public class ShardStatsIT extends ESIntegTestCase {
  @Override
  protected Collection<Class<? extends Plugin>> nodePlugins() {
    return Arrays.asList(LtrQueryParserPlugin.class, TestExpressionsPlugin.class);
  }

  @Override
  protected int numberOfShards() {
    return 2;
  }

  protected void createIdx() {
    prepareCreate("idx").setMapping("type=text");

    for (int i = 0; i < 4; i++) {
      indexDoc(i);
    }
    refreshIndex();
  }

  protected void indexDoc(int id) {
    client()
        .prepareIndex("idx")
        .setRouting(((id % 2) == 0) ? "a" : "b")
        .setSource("s", "zzz")
        .get();
  }

  protected void refreshIndex() {
    client().admin().indices().prepareRefresh("idx").get();
  }

  public void testDfsExplorer() throws Exception {
    createIdx();

    QueryBuilder q = new TermQueryBuilder("s", "zzz");

    ExplorerQueryBuilder eq = new ExplorerQueryBuilder().query(q).statsType("min_raw_df");

    final SearchResponse r =
        client()
            .prepareSearch("idx")
            .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
            .setQuery(eq)
            .get();

    assertNoFailures(r);

    SearchHits hits = r.getHits();
    assertThat(hits.getAt(0).getScore(), equalTo(4.0f));
  }

  public void testNonDfsExplorer() throws Exception {
    createIdx();

    QueryBuilder q = new TermQueryBuilder("s", "zzz");

    ExplorerQueryBuilder eq = new ExplorerQueryBuilder().query(q).statsType("min_raw_df");

    final SearchResponse r =
        client().prepareSearch("idx").setSearchType(SearchType.QUERY_THEN_FETCH).setQuery(eq).get();

    assertNoFailures(r);

    SearchHits hits = r.getHits();
    assertThat(hits.getAt(0).getScore(), equalTo(2.0f));
  }

  public void testDfsTSQ() throws Exception {
    createIdx();

    TermStatQueryBuilder tsq =
        new TermStatQueryBuilder()
            .expr("df")
            .aggr("min")
            .posAggr("min")
            .terms(new String[] {"zzz"})
            .fields(new String[] {"s"});

    final SearchResponse r =
        client()
            .prepareSearch("idx")
            .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
            .setQuery(tsq)
            .get();

    assertNoFailures(r);

    SearchHits hits = r.getHits();
    assertThat(hits.getAt(0).getScore(), equalTo(4.0f));
  }

  public void testNonDfsTSQ() throws Exception {
    createIdx();

    TermStatQueryBuilder tsq =
        new TermStatQueryBuilder()
            .expr("df")
            .aggr("min")
            .posAggr("min")
            .terms(new String[] {"zzz"})
            .fields(new String[] {"s"});

    final SearchResponse r =
        client()
            .prepareSearch("idx")
            .setSearchType(SearchType.QUERY_THEN_FETCH)
            .setQuery(tsq)
            .get();

    assertNoFailures(r);

    SearchHits hits = r.getHits();
    assertThat(hits.getAt(0).getScore(), equalTo(2.0f));
  }
}
