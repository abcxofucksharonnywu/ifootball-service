String.prototype.startWith = function (str) {
    var reg = new RegExp("^" + str)
    return reg.test(this)
}

String.prototype.endWith = function (str) {
    var reg = new RegExp(str + "$")
    return reg.test(this)
}
var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var session = require('express-session')
var index = require('./routes/index');
var api = require('./routes/api');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json({limit: "50mb"}));
app.use(bodyParser.urlencoded({limit: "50mb", extended: true, parameterLimit: 50000}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
// MongoStore = require('connect-mongo')(session)
app.use(session({
    secret: 'ifootball',
    saveUninitialized: true,
    // cookie: {
    //     maxAge: 365 * 3600000
    // },
    resave: true,
    // store: new MongoStore({url: 'mongodb://localhost:27017/ifootball'})
}))


app.use('*', function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    var url = req.originalUrl
    if (!url.startWith("/api")) {
        var isLogin = req.session != null && req.session.user != null
        var isFromLogin = url == "/user/login"
        if (isLogin && isFromLogin) {
            return res.redirect("/")
        } else if (!isLogin && !isFromLogin) {
            if (!isFromLogin) {
                return res.redirect("/user/login")
            }
        }
    }
    next()

})

app.use('/', index);
app.use('/api', api);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
