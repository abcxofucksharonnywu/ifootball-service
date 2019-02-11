var express = require('express');
var router = express.Router();
var db = require('monk')('localhost/ifootball')
var request = require('request');
// var host = "http://localhost:8080/ifootball/"
var host = "http://localhost:8080"

router.get('/user/logout', function (req, res, next) {
    req.session.destroy(function (err) {
        res.send({code: 302, url: '/user/login'})
    })
})


router.get('/user/login', function (req, res, next) {
    req.session.regenerate(function () {
        var name = req.query.name
        var password = req.query.password
        if (name && password) {
            request({
                method: 'get',
                url: host + '/user/login',
                headers: {
                    'Content-Type': 'application/json'
                },
                qs: {
                    email: name,
                    password: password,
                    admin: true
                }
            }, function (error, response, body) {
                if (!error && response.statusCode == 200) {
                    req.session.user = JSON.parse(body)
                    res.send({code: 302, url: '/'})
                } else {
                    res.send({code: 400, message: "账号或者密码错误"})
                }

            });
        } else {
            res.send({code: 400, message: "账号或者密码错误"})
        }

    })
});

router.get('/tweet/list', function (req, res, next) {
    var search = req.query.search
    var index = req.query.index
    if (req.session.user != null) {
        request({
            method: 'get',
            url: host + '/tweet/list2',
            headers: {
                'Content-Type': 'application/json'
            },
            qs: {
                tweetType: "PRO",//pro
                uid: req.session.user.id,
                keyword: search,
                pageIndex: index-1,
                pageSize: 30
            }
        }, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                var json = JSON.parse(body)
                res.send({code: 200, content: json.content, total: json.total});
            } else {
                res.send({code: 400, message: "获取推文失败"})
            }
        });
    } else {
        res.send({code: 400, message: "请尝试再次登录"})
    }
});


router.get('/tweet/delete', function (req, res, next) {
    var tid = req.query.tid
    if (req.session.user != null) {
        request({
            method: 'delete',
            url: host + '/tweet',
            headers: {
                'Content-Type': 'application/json'
            },
            qs: {
                tid: tid,
                uid: req.session.user.id
            }
        }, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                res.send({code: 302, url: '/'})
            } else {
                res.send({code: 400, message: "推文不存在或删除失败"})
            }
        });
    } else {
        res.send({code: 400, message: "请尝试再次登录"})
    }
});


router.post('/tweet/add', function (req, res, next) {
    var tid = req.body.tid
    var title = req.body.title
    var content = req.body.content
    var summary = req.body.summary
    var update = req.body.update == 'true' ? true : false
    if (req.session.user != null) {
        var tweet = update ? {
            id: tid,
            title: title,
            content: content,
            summary: summary
        } : {
            uid: req.session.user.id,
            icon: req.session.user.avatar,
            name: req.session.user.name,
            title: title,
            content: content,
            summary: summary,
            tweetType: "PRO"

        }
        request({
            method: 'post',
            url: host + '/tweet2',
            headers: {
                'Content-Type': 'application/json'
            },
            qs: {
                tweet: JSON.stringify(tweet),
                update: update
            }
        }, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                var json = JSON.parse(body)
                if (update) {
                    res.send({code: 200, message: "推文保存成功"})
                } else {
                    res.send({code: 302, url: '/tweet/content?tid=' + json.id, message: "推文保存成功"})
                }

            } else {
                res.send({code: 400, message: "推文保存失败"})
            }
        });
    } else {
        res.send({code: 400, message: "请尝试再次登录"})
    }
});


router.getTweet = function getTweet(uid, tid, callback) {
    request({
        method: 'get',
        url: host + '/tweet',
        headers: {
            'Content-Type': 'application/json'
        },
        qs: {
            uid: uid,
            tid: tid
        }
    }, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            var json = JSON.parse(body)
            callback(json)
        } else {
            callback(null)
        }
    });
}


module.exports = router;