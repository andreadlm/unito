let articlesBigContainer = undefined;
let articlesSmallContainer = undefined;
let isEditor = false;

// Shows an error box at the top of the $parent DOM object containing the specified message
function showError($parent, msg) {
    let $self = $(`<div class="error-message"><p>${msg}</p></div>`).prependTo($parent);
    $self.append('<button type="button">X</button>').click(() => { $self.remove() });
}

// Converts special characters in the string in HTML escape sequences to
// prevent XSS attacks
function formatXSS(string) {
    let tagsToReplace = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;'
    };
    return string.replace(/[&<>]/g, (tag) => tagsToReplace[tag] || tag);
}

// Container for the articles. It maintains references to the current displayed articles,
// the current section and all the information associated (the current tag/keyword).
class ArticlesContainer {
    static titles = {
        'newest': 'Latest articles',
        'noisiest': 'Noisiest articles',
        'favourites': 'My favourites',
        'read-later': 'Read later',
        'tag': 'Tag ',
        'keyword': 'Search for '
    };

    constructor(section) {
        this.section = section;
        this.articles = [];
    }

    // Deletes all the articles. Sets all the information about the currently
    // displayed section to the default values.
    reset() {
        this.tag = undefined;
        this.keyword = undefined;
        this.articles.forEach(article => article.delete());
        this.articles = [];
    }
}

// Container for the big articles.
class ArticlesBigContainer extends ArticlesContainer {
    constructor(section) {
        super(section);
        this.tag = undefined;
        this.keyword = undefined;
        this.defaultNewArticlesNum = 3;
        this.selectedArticle = undefined;
    }

    // Associates all the buttons in the container to the corresponding click actions.
    create() {
        let $self = $('#articles');
        let $backButton = $self.find('#back-button');
        let $loadMoreButton = $self.children('#load-more-button');

        $backButton.click(() => { this.loadNewest(true); });

        $loadMoreButton.click(() => { this.loadCurrent(false); });
    }

    // Sets the article and all the information associated to the new section.
    // A call to this method destroys all the articles currently displayed.
    setContent(articles, section, tag, keyword) {
        this.reset();

        this.section = section;
        this.tag = tag;
        this.keyword = keyword;

        this.updateTitle(section);
        this.addContent(articles);
    }

    // Updates the current title.
    // A title can be static or dynamic. Dynamically created titles contain
    // information about the current tag or the current searched keyword.
    updateTitle(section) {
        let $self = $('#articles');
        let $title = $self.find('.section-title-title');
        let $backButton = $self.find('#back-button');

        let sectionTitle = ArticlesContainer.titles[section];
        if(section === 'tag' && this.tag !== undefined) $title.text(sectionTitle + this.tag);
        else if(section === 'keyword' && this.keyword !== undefined) $title.text(sectionTitle + this.keyword);
        else $title.text(ArticlesContainer.titles[section]);

        if(this.section === 'newest') $backButton.hide();
        else $backButton.show();
    }

    // Adds new articles to the currently displayed ones.
    // If the number of articles passed to the function is lower than defaultArticlesNum the 'load more' button
    // is replaced by the 'end list' banner.
    addContent(articles) {
        let $self = $('#articles');
        let $loadMoreButton = $self.children('#load-more-button');
        let $listEnd = $self.children('#list-end');
        let $articlesContainer = $self.children('#articles-big-list');

        if(this.section === 'noisiest') { // Noisiest list do not allow to load other articles
            $loadMoreButton.hide();
            $listEnd.hide();
        } else if(articles.length < this.defaultNewArticlesNum) {
            $loadMoreButton.hide();
            $listEnd.show();
        } else {
            $loadMoreButton.show();
            $listEnd.hide();
        }

        if(articles.length === 0) return;

        let articlesObj = articles.map(article => new ArticleBig(article));
        articlesObj.forEach(article => article.create($articlesContainer));

        this.articles = this.articles.concat(articlesObj);
    }

    // Deletes the currently displayed article with a specific id.
    deleteArticle(articleId) {
        this.articles.find(article => article.config.id === articleId).delete();
    }

    // Load articles of type newest
    loadNewest(replace) {
        this.loadArticles({
            type: 'newest',
            from: replace ? 0 : this.articles.length,
            num: this.defaultNewArticlesNum
        }, replace);
    }

    // Load articles of type noisiest
    loadNoisiest(replace) {
        this.loadArticles({
            type: 'noisiest',
            from: replace ? 0 : this.articles.length,
            num: this.defaultNewArticlesNum
        }, replace);
    }

    // Load articles of type favourite
    loadFavourites(replace) {
        this.loadArticles({
            type: 'favourites',
            from: replace ? 0 : this.articles.length,
            num: this.defaultNewArticlesNum
        }, replace);
    }

    // Load articles of type read later
    loadReadLater(replace) {
        this.loadArticles({
            type: 'read-later',
            from: replace ? 0 : this.articles.length,
            num: this.defaultNewArticlesNum
        }, replace);
    }

    // Load articles of type tag associated to a specified tag
    loadTag(replace, tag) {
        this.loadArticles({
            type: 'tag',
            tag: tag,
            from: replace ? 0 : this.articles.length,
            num: this.defaultNewArticlesNum
        }, replace);
    }

    // Load articles of type keyword associated to a specified keyword
    loadKeyword(replace, keyword) {
        this.loadArticles({
            type: 'keyword',
            keyword: keyword,
            from: replace ? 0 : this.articles.length,
            num: this.defaultNewArticlesNum
        }, replace);
    }

    // Load articles of the current type
    loadCurrent(replace) {
        switch(this.section) {
            case 'newest': this.loadNewest(replace); break;
            case 'noisiest': this.loadNoisiest(replace); break;
            case 'read-later': this.loadReadLater(replace); break;
            case 'favourites': this.loadFavourites(replace); break;
            case 'tag': this.loadTag(replace, this.tag); break;
            case 'keyword': this.loadKeyword(replace, this.keyword); break;
        }
    }

    // Call to the remote service get_articles.
    // Articles are downloaded and then added or replaced to the current ones.
    loadArticles(request, replace) {
        $.ajax({
            url: '/Vuvuzela/services/article/get_articles.php',
            type: 'GET',
            data: request,
            success: (msg) => {
                if(msg['status'] === 'error') {
                    this.showError();
                    console.log(msg['errorMsg']);
                } else if(msg['status'] === 'success') {
                    let articles = msg['data'];
                    if(replace)
                        this.setContent(articles, request['type'], request['tag'], request['keyword'])
                    else
                        this.addContent(articles)
                }
            },
            error: (jqXHR, textStatus, errorThrow)  => {
                this.showError();
                console.log(jqXHR.responseText + "\n" + textStatus + "\n" + errorThrow)
            },
            dataType: 'json'
        });
    }

    // Shows an error banner inside the articles container.
    showError() {
        showError($('#articles').children('#articles-big-list'), 'Error downloading articles');
    }
}

// Article big container. Each article is associated to a configuration object containing the information
// retrieved from the db.
class ArticleBig {
    constructor(config) {
        this.config = config;
        this.idDOM = `article-big-${this.config.id}`;
        this.buttons = undefined;
        this.contentShown = false;
        this.commentSection = undefined;
    }

    // Dynamically creates a new article big inside the $parent container.
    create($parent) {
        let $self = $(`<article id="${this.idDOM}" class="article-big"></article>`).appendTo($parent);

        $(`<div class="article-big-header">` +
            `<div class="article-big-image" style="background-image: url('/Vuvuzela/img/articles/${this.config['img']}');"></div>` +
            `<h6 class="article-big-category">${this.config['category']}</h6>` +
            `<h3 class="article-big-title">${this.config['title']}</h3>` +
            `<h4 class="article-big-description">${this.config['description']}</h4>` +
            `</div>`).appendTo($self);

        let $topBar = $(`<div class="article-big-top-bar">` +
            `<h6 class="article-big-author-date">By ${this.config['author']} • ${this.config['date']} </h6>` +
            `</div>`).appendTo($self);

        $(`<div class="article-big-content"></div>`).appendTo($self);

        $self.find('.article-big-image, .article-big-title').click(() => this.showContent());

        this.buttons = {
            'noiseButton': new VuvuzelaButton(this, !!this.config['noised'] /* int -> bool */),
            'favouriteButton': new FavouriteButton(this, !!this.config['favourite']),
            'readLaterButton': new ReadLaterButton(this, !!this.config['read_later'])
        };
        if(isEditor) this.buttons['deleteButton'] = new DeleteButton(this); // Delete operation only allowed to editors
        Object.keys(this.buttons).forEach(type => this.buttons[type].create($topBar)); // Creates the buttons specified in this.buttons
    }

    // Shows text and the bottom bar.
    showContent() {
        if(this.contentShown) return;
        if(articlesBigContainer.selectedArticle !== undefined)
            articlesBigContainer.selectedArticle.hideContent(); // Hides currently selected article
        this.loadText(); // Downloads text from db
    }

    // Shows the text, initializes comments button listener.
    showText($paragraphs) {
        let $self = $(`#${this.idDOM}`);
        let $content = $self.children('.article-big-content');

        articlesBigContainer.selectedArticle = this;

        let $textContainer = $(`<div class="article-big-text"></div>`).appendTo($content);
        let $bottomBar = $(`<div class="article-big-bottom-bar"></div>`).appendTo($content);

        $paragraphs.map(row => $(`<p>${row}</p>`).appendTo($textContainer));

        $('<button class="article-big-collapse-button" type="button">⮝</button>')
            .appendTo($bottomBar)
            .click(() => this.hideContent());

        $('<button class="button-fill" type="button">Show comments</button>')
            .appendTo($bottomBar)
            .click((event) => {
                this.showComments();
                event.target.remove();
            });

        this.contentShown = true;

        this.scrollToTitle(); // Scroll animation
    }

    // Creates a new comments section for this article
    showComments() {
        let $self = $(`#${this.idDOM}`);
        let $content = $self.children('.article-big-content');

        this.commentSection = new CommentSection(this.config.id);
        this.commentSection.create($content);
    }

    // Call to get_text service.
    // When text is retrieved from db, it is put inside the article container.
    loadText() {
        $.ajax({
            url: '/Vuvuzela/services/article/get_text.php',
            type: 'GET',
            data: {
                article_id: this.config['id']
            },
            success: (msg) => {
                if(msg['status'] === 'error') {
                    this.showError('Error downloading text');
                    console.log(msg['errorMsg']);
                } else if(msg['status'] === 'success') {
                    this.showText(msg['data']['text'].split('\n'));
                }
            },
            error: (jqXHR, textStatus, errorThrown )  => {
                this.showError('Error downloading text');
                console.log("Error: " + errorThrown + " " + textStatus)
            },
            dataType: 'json'
        });
    }

    // Animation: smoothly scrolls to the title position.
    scrollToTitle() {
        let $self = $(`#${this.idDOM}`);

        $('html, body').animate({
            scrollTop: $self.find('.article-big-category').offset().top
        }, 1000);
    }

    // Removes text and bottom bar.
    hideContent() {
        let $self = $(`#${this.idDOM}`);
        let $content = $self.children('.article-big-content');

        if(this.commentSection !== undefined) {
            this.commentSection.delete();
            this.commentSection = undefined;
        }
        $content.empty();
        this.contentShown = false;
        articlesBigContainer.selectedArticle = undefined;
    }

    // Deletes the article and removes it from the DOM.
    delete() {
        let $self = $(`#${this.idDOM}`);
        if(this.commentSection !== undefined) {
            this.commentSection.delete();
            this.commentSection = undefined;
        }
        $self.remove();
        Object.keys(this.buttons).forEach(type => this.buttons[type].delete());
    }

    // Shows an error message inside the article text container.
    showError(msg) {
        showError($(`#${this.idDOM}`).children('.article-big-content'), msg)
    }
}

// Action button class. An action button performs a certain type of action (noise, add/remove to read-later/favourites).
// Each button is identified by an inactive text (when it has never been pressed) and an active text. It is displayed
// using a certain css class.
// This class is not intended to be instantiated. Only children classes are used.
class ActionButton {
    constructor(article, active, activeText, inactiveText, type, cssClass) {
        this.article = article;
        this.articleID = article.config['id'];
        this.idDOM = `article-big-${type}-button-${this.articleID}`;
        this.active = active;
        this.activeText = activeText;
        this.inactiveText = inactiveText;
        this.type = type;
        this.cssClass = cssClass;
    }

    // Creates the button inside the $parent container.
    create($parent) {
        $(`<button id="${this.idDOM}" class="${this.cssClass}" type="button">${this.active ? this.activeText : this.inactiveText}</button>`)
            .appendTo($parent)
            .click(() => this.onClick());
    }

    // Call to the actions script.
    // The call is parametrized on the button configuration.
    // andThen function allows to execute a children-defined function on success.
    onClick(andThen) {
        let $self = $(`#${this.idDOM}`);
        $.ajax({
            url: '/Vuvuzela/services/article/actions.php',
            type: 'POST',
            data: {
                type: this.type,
                article: this.articleID,
                action: this.active ? 'remove' : 'add'
            },
            success: (message) => {
                if(message['status'] === 'error') {
                    this.article.showError('Error cannot perform action');
                    console.log("Error: " + message['errorMsg']);
                } else if(message['status'] === 'success') {
                    this.active = !this.active;
                    $self.text(this.active ? this.activeText : this.inactiveText)
                }
                if(andThen !== undefined) andThen();
            },
            error: (jqXHR, textStatus, errorThrown )  => {
                this.article.showError('Error cannot perform action');
                console.log("Error: " + errorThrown + " " + textStatus)
            },
            dataType: 'json'
        });
    }

    delete() {
        $(`#${this.idDOM}`).remove();
    }
}

class VuvuzelaButton extends ActionButton {
    constructor(article, active) {
        super(
            article,
            active,
            '🤫 Shh!',
            '🎺 Make noise!',
            'noise',
            'button-accent'
        );
    }

    onClick() {
        if(!this.active)
            new Audio('/Vuvuzela/audio/Vuvuzela-sound.mp3')
                .play()
                .then(() => {});
        super.onClick(() => { articlesSmallContainer.loadArticles(); });
    }
}

class ReadLaterButton extends ActionButton {
    constructor(article, active) {
        super(
            article,
            active,
            '🗑️ Remove from read later',
            '💾 Save for later',
            'read_later',
            'button-fill'
        );
    }
}

class FavouriteButton extends ActionButton {
    constructor(article, active) {
        super(
            article,
            active,
            '🗑️ Remove from favourites',
            '🌟 Add to favourites',
            'favourite',
            'button-fill'
        );
    }
}

class DeleteButton {
    constructor(article) {
        this.article = article;
        this.articleID = article.config['id'];
        this.idDOM = `article-big-delete-button-${this.articleID}`;
    }

    create($parent) {
        $(`<button id="${this.idDOM}" class="button-fill" type="button">❌ Delete</button>`)
            .appendTo($parent)
            .click(() => this.onClick());
    }

    // Does not call the super-class method. Another service, delete_article.php is called
    // instead.
    onClick() {
        $.ajax({
            url: '/Vuvuzela/services/article/delete_article.php',
            type: 'POST',
            data: {
                article_id: this.articleID
            },
            success: (msg) => {
                if(msg['status'] === 'error')
                    console.log(msg['errorMsg']);
                else if(msg['status'] === 'success')
                    articlesBigContainer.deleteArticle(this.articleID)
            },
            error: (jqXHR, textStatus, errorThrow)  => {
                console.log(jqXHR.responseText + "\n" + textStatus + "\n" + errorThrow)
            },
            dataType: 'json'
        });
    }

    delete() {
        $(`#${this.idDOM}`).remove();
    }
}

// Comment section class. A comment section is displayed at the bottom of an article ant contains a list
// of displayed comments.
class CommentSection {
    constructor(articleID) {
        this.articleID = articleID;
        this.idDOM = `article-big-comments-${articleID}`;
        this.lastCommentID = undefined;
        this.comments = [];
    }

    create($parent) {
        let $self = $(`<div id="${this.idDOM}" class="article-big-comments"></div>`)
            .appendTo($parent);
        let $button = $(`<button class="button-fill article-big-comments-button-publish" type="button" disabled>Publish</button>`)
            .appendTo($self)
            .click(() => this.writeComment());
        let $input = $('<input id="comment-input" type="text" placeholder="Write comment" autocomplete="off">')
            .appendTo($self);
        // Button enabled only with non-empty input text
        $input.keyup(() => $button.prop('disabled', $input.val().trim() === ''));

        $('<div class="article-big-comments-list"></div>').appendTo($self);

        $('<button class="button-underline article-big-comments-button-show-more">Show more</button>')
            .appendTo($self)
            .click(() => this.loadComments());

        this.loadComments();
    }

    // Adds new comments to the displayed ones.
    addContent(comments) {
        let $self = $(`#${this.idDOM}`);
        let $container = $self.children('.article-big-comments-list');

        if(comments.length === 0) return;

        this.lastCommentID = comments[comments.length - 1].id;
        let commentsObj = comments.map(comment => new Comment(comment, false));
        commentsObj.forEach(comment => comment.create($container));
        this.comments = this.comments.concat(commentsObj);
    }

    // Shows a comment with a specified style-modifier.
    showComment(comment, modifier) {
        let $self = $(`#${this.idDOM}`);
        let $container = $self.children('.article-big-comments-list');

        let commentObj = new Comment(comment, modifier);
        commentObj.create($container);
        if(this.comments.length === 0) this.lastCommentID = comment['id'];
        this.comments.push(commentObj);
    }

    // Call to the get_comments service.
    // It retrieves a certain number of comments from the db and shows them.
    loadComments() {
        $.ajax({
            url: '/Vuvuzela/services/comment/get_comments.php',
            type: 'GET',
            data: {
                first: this.lastCommentID === undefined ? -1 : this.lastCommentID,
                num: 3,
                article: this.articleID
            },
            success: (msg) => {
                if(msg['status'] === 'error') {
                    this.showError('Error downloading comments');
                    console.log(msg['errorMsg']);
                } else if(msg['status'] === 'success') {
                    if(msg['data'].length < 3)
                        $(`#${this.idDOM}`).children('.article-big-comments-button-show-more').hide();
                    this.addContent(msg['data']);
                }
            },
            error: (jqXHR, textStatus, errorThrow)  => {
                this.showError('Error downloading comments');
                console.log(jqXHR.responseText + "\n" + textStatus + "\n" + errorThrow)
            },
            dataType: 'json'
        });
    }

    // Call to the write_comment service.
    // The newly created comment is then shown.
    writeComment() {
        let $self = $(`#${this.idDOM}`);
        let $input = $self.children('#comment-input');

        $.ajax({
            url: '/Vuvuzela/services/comment/write_comment.php',
            type: 'POST',
            data: {
                text: $input.val(),
                article: this.articleID
            },
            success: (msg) => {
                if(msg['status'] === 'error') {
                    this.showError('Error uploading comment');
                    console.log(msg['errorMsg']);
                } else if(msg['status'] === 'success') {
                    this.showComment(msg['data'], 'highlighted' /* Style modifier */);
                }
            },
            error: (jqXHR, textStatus, errorThrow)  => {
                this.showError('Error uploading comment');
                console.log(jqXHR.responseText + "\n" + textStatus + "\n" + errorThrow)
            },
            dataType: 'json'
        });

        $input.val('');
    }

    // Deletes the Comments section by deleting first all the displayed comments.
    delete() {
        this.comments.forEach(comment => comment.delete());
        this.comments = [];
        $(`#${this.idDOM}`).remove();
    }

    // Shows an error message banner inside the comments list section with a specified message.
    showError(msg) {
        showError($(`#${this.idDOM}`).children('.article-big-comments-list'), msg);
    }
}

// Comment class. Each comment is associated to a config object retrieved from the database and a style modifier.
class Comment {
    constructor(config, modifier) {
        this.config = config;
        this.modifier = modifier;
    }

    // Creates a comment inside the $parent container.
    create($parent) {
        let html =
            `<div id="comment-${this.config['id']}" class="comment ${this.modifier ? `comment-${this.modifier}` : ''}">` +
                `<div class="comment-header">` +
                    `<span class="comment-author">${this.config['author']}</span>` +
                    `<span class="comment-date">${this.config['date']}</span>` +
                `</div>` +
                `<div class="comment-text">${this.config['text']}</div>` +
            `</div>`;
        this.modifier ? $(html).prependTo($parent) : $(html).appendTo($parent);
    }

    // Deletes the comment from the DOM.
    delete() {
        $(`comment-${this.config['id']}`).remove();
    }
}

// Container form small articles.
class ArticlesSmallContainer extends ArticlesContainer {
    // Binds the listener to the 'see all' button.
    create() {
        let $self = $('#articles-small');
        let $seeAllButton = $self.find('#see-all-button');

        $seeAllButton.click(() => {
            articlesBigContainer.setContent(
                this.articles.map(article => article.config),
                'noisiest');
        });
    }

    loadArticles() {
        $.ajax({
            url: '/Vuvuzela/services/article/get_articles.php',
            type: 'GET',
            data: {
                type: 'noisiest',
                from: 0,
                num: 5
            },
            success: (msg) => {
                if(msg['status'] === 'error') {
                    this.showError();
                    console.log(msg['errorMsg']);
                } else if(msg['status'] === 'success') {
                    let articles = msg['data'];
                    this.setContent(articles, 'noisiest');
                }
            },
            error: (jqXHR, textStatus, errorThrow)  => {
                this.showError();
                console.log(jqXHR.responseText + "\n" + textStatus + "\n" + errorThrow)
            },
            dataType: 'json'
        });
    }

    // Sets the article and all the information associated to the new section.
    // A call to this method destroys all the articles currently displayed.
    setContent(articles, section) {
        let $self = $('#articles-small');
        let $sectionTitle = $self.find('#section-title-title');
        let $articlesContainer = $self.children('#articles-small-list');

        this.articles.forEach(article => article.delete());
        this.articles = [];
        this.section = section;

        $sectionTitle.text(ArticlesContainer.titles[this.section]);

        if(this.articles.length > 0)
            articles.forEach(article => article.delete());

        let articlesObj = articles.slice(0, 5).map(article => new ArticleSmall(article));
        articlesObj.forEach(article => article.create($articlesContainer));

        this.articles = articlesObj;
    }

    // Shows an error message inside the article text container.
    showError(msg) {
        showError($('#articles-small-list'), msg)
    }
}

// Article small class. Each article small is associated to a config object retrieved from the db.
class ArticleSmall {
    constructor(config) {
        this.config = config;
        this.idDOM = `article-small-${this.config.id}`;
    }

    // Creates the article small inside the $parent container.
    create($parent) {
        $(`<article id=${this.idDOM} class="article-small">
               <div class="article-small-article">
                   <h6 class="article-small-category">${this.config['category']}</h6>
                   <h5 class="article-small-title">${this.config['title']}</h5>
                   <h6 class="article-small-author">By ${this.config['author']}</h6>
               </div>
               <div class="article-small-noise-count">🎺 ${this.config['noise']}</div>
           </article>`).appendTo($parent);
    }

    // Deletes the article from the DOM.
    delete() {
        $(`#${this.idDOM}`).remove();
    }
}

// Creates a tag in the $parent container on the associate config object.
function createTag($parent, config) {
    $(`<span id="tag-${config['tag_name']}" class="tag">${config['tag_name']}</span>`)
        .appendTo($parent)
        .click(() => { articlesBigContainer.loadTag(true, config['tag_name'] )});
}

// Authors slider. Implements the navigation of the authors carousel.
class AuthorSlider {
    constructor() {
        this.authorsCount = 0;
        this.currentAuthor = undefined;
    }

    create() {
        $('#button-prev').click(() => this.prevClick());
        $('#button-next').click(() => this.nextClick());
    }

    setContent(authors) {
        let $slider = $('#slider');

        authors.forEach(author => {
            $(`<article class="author">
                   <img class="author-img" src="/Vuvuzela/img/authors/${author['img']}" alt="profile picture">
                   <h3 class="author-title">${author['name']}</h3>
               </article>`).appendTo($slider);
        });

        this.authorsCount = authors.length;
        this.currentAuthor = 0;

        $('#wrapper').height($slider.outerHeight() + 20);

        // Resets slider content position: necessary to manage a responsive layout
        $(window).resize(() => {
            $slider.css({
                position: 'absolute',
                top: '0',
                left: '0'
            });

            this.currentAuthor = 0;
        })
    }

    prevClick() {
        if(this.currentAuthor > 0) {
            let elementWidth = $(".author").width();
            $('#slider').animate({left: "+=" + elementWidth + "px"}, 500);
            this.currentAuthor--;
        }
    }

    nextClick() {
        if(this.currentAuthor < this.authorsCount - 1) {
            let elementWidth = $(".author").width();
            $('#slider').animate({left: "-=" + elementWidth + "px"}, 500);
            this.currentAuthor++;
        }
    }
}

// Uploads the editor image to the server by calling the editor_info service.
function uploadEditorImg() {
    let formData = new FormData(); // FormData() helps to compile a request module.
    formData.append('type', 'set');
    formData.append('img', $('#editor-img-input')[0].files[0]);
    $.ajax({
        url: '/Vuvuzela/services/editor/editor_info.php',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        success: (msg) => {
            if(msg['status'] === 'error') {
                showError($('#articles'), 'Error uploading image');
                console.log(msg['errorMsg']);
            } else if(msg['status'] === 'success') {
                let info = msg['data'];
                $('#editor-info-img').attr('src', '/Vuvuzela/' + info['img']);
                $('#editor-info-title').text(info['name']);
            }
        },
        error: (jqXHR, textStatus, errorThrow)  => {
            showError($('#articles'), 'Error uploading image');
            console.log(jqXHR.responseText + "\n" + textStatus + "\n" + errorThrow);
        },
        dataType: 'json'
    });
}

// Call to the convenience init service. It helps to reduce services calls to initialize content.
function initContent(articlesBigContainer, articlesSmallContainer, authorsSlider) {
    $.ajax({
        url: '/Vuvuzela/services/init.php',
        type: 'GET',
        data: {},
        success: (message) => {
            if(message['status'] === 'error') {
                showError($('#articles'), 'Error downloading content');
                console.log(message['errorMsg']);
            } if(message['status'] === 'success') {
                let data = message['data'];

                isEditor = data['role'] === 'editor';

                articlesBigContainer.setContent(data['newestArticles'], 'newest', '*');
                articlesSmallContainer.setContent(data['noisiestArticles'], 'noisiest');

                authorsSlider.setContent(data['authors']);

                if(isEditor) {
                    $.get('/Vuvuzela/pages/editor_section.html', (page) => {
                        $('aside').prepend(page);

                        $('#editor-info-img').attr('src', "/Vuvuzela/" + data['editor_info']['img']);
                        $('#editor-info-title').text(data['editor_info']['name']);

                        let $editorImgInput = $('#editor-img-input');
                        $('#overlay').click(() => $editorImgInput.click());
                        $editorImgInput.change(() => { if($editorImgInput[0].files.length !== 0) uploadEditorImg(); });
                    });
                }

                data['tags'].forEach(tag => createTag($('#filter-tags'), tag));
            }
        },
        error: (jqXHR, textStatus, errorThrow)  => {
            showError($('#articles'), 'Error downloading content');
            console.log(jqXHR.responseText + "\n" + textStatus + "\n" + errorThrow);
        },
        dataType: 'json'
    });
}

// Initialization calls
$(document).ready(() => {
    let $aside = $('aside');

    articlesBigContainer = new ArticlesBigContainer('newest');
    articlesBigContainer.create();

    let authorsSlider = new AuthorSlider($aside);
    authorsSlider.create();

    articlesSmallContainer = new ArticlesSmallContainer('noisiest');
    articlesSmallContainer.create();

    initContent(articlesBigContainer, articlesSmallContainer, authorsSlider);

    $('#favourites-button').click(() => { articlesBigContainer.loadFavourites(true); });
    $('#read-later-button').click(() => { articlesBigContainer.loadReadLater(true); });

    let $searchInputButton = $('#search-input-button');
    let $searchInputText = $('#search-input-text');
    // Button enabled only with text in input box
    $searchInputText.keyup(() => $searchInputButton.prop('disabled', $searchInputText.val().trim() === ''));
    $searchInputButton.click(() => { articlesBigContainer.loadKeyword(true, formatXSS($searchInputText.val())); });
});