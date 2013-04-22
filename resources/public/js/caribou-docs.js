
$( function() {
  // This tricky is necessary because clojure-markdown spits out the
  // class: brush for the wrong tag.
  $("code[class^=brush]").each(function(index, el) {
    $(el).parent().attr("class", $(el).attr("class"));
  });

  SyntaxHighlighter.all();
});
