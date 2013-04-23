
$( function() {
  // This tricky is necessary because clojure-markdown spits out the
  // class: brush for the wrong tag.
  $("code[class^=brush]").each(function(index, el) {
    $(el).parent().attr("class", $(el).attr("class"));
    var html = $(el).html();
    $(el).parent().empty().html(html);
  });

  SyntaxHighlighter.all();
});
