var express = require('express');
var router = express.Router();
var api = require('./api');

router.get('/', function (req, res, next) {
    res.render('index', {title: '欢迎使用足球狗CMS', name: req.session.user.name});
});


router.get('/user/login', function (req, res, next) {
    res.render('login', {title: "欢迎使用足球狗CMS"});
});


router.get('/tweet', function (req, res, next) {
    res.render('tweet', {}, function (err, output) {
            if (!err) {
                res.json({
                    code: 200, content: output
                })
            } else {
                res.send({code: 400, message: err.message})
            }
        }
    )
});


router.get('/tweet/content', function (req, res, next) {
    var tid = req.query.tid;
    var uid = req.session.user.id;
    api.getTweet(uid, tid, function (json) {
        res.render('tweet-content', {
            title: '欢迎使用足球狗CMS',
            name: req.session.user.name,
            user: req.session.user,
            tweet: json
        });
    })
});


module.exports = router;
