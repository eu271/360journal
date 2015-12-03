/* global $, FormData*/
/*
 * The MIT License
 *
 * Copyright 2015 Eugenio Ochoa.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var pages = {
  'login': {
    'path': '/login',
    'class': 'login',
    'isSetup': false,
    'setup': setupLogin
  },
  'register': {
    'path': '/register',
    'class': 'register',
    'isSetup': false,
    'setup': null
  },
  'entries': {
    'path': '/getEntries',
    'class': 'entries',
    'isSetup': false,
    'setup': setupEntries,
    'update': addEnties
  },
  'editor': {
    'path': '/addEntry',
    'class': 'editor',
    'isSetup': false,
    'setup': setupEditor
  },
  'stats': {
    'path': '/stats',
    'class': 'stats',
    'isSetup': false
  }
}

var changePage = function (page) {
  // Clear the webpage.
  $('.page').css('display', 'none')

  $('.' + pages[page].class).css('display', 'block')
}

var isUserLogin = function () {
  return pages['login'].userLogin
}
/**
 * Setup Login form.
 */
var setupLogin = function () {
  $('form.login').submit(function (ev) {
    $.post(pages['login'].path, $(this).serialize(), function (data) {
      var resp = JSON.parse(data)
      if (resp.valid) {
        pages['login'].userLogin = true

        changePage('editor')
      }else {
        $('.login-error').css('display', 'block')
      }
    })

    ev.preventDefault()
  })

  pages['login'].setup = true
}

var setupEditor = function () {
  $('form.entry-editor').submit(function (ev) {
    var data = new FormData()
    data.append('title', $('form.entry-editor[name="title"]'))
    data.append('content', $('form.entry-editor[name="content"]'))
    data.append('date', new Date().getTime())
    data.append('photo', $('#photoInput').prop('files')[0])

    $.ajax({
      type: 'POST',
      url: pages['editor'].path,
      data: data,
      processData: false,
      contentType: false,
      success: function (data) {
        alert(data)
      }
    })
    ev.preventDefault()
  })

  pages['editor'].isSetup = true
}

var setupEntries = function () {
  $.get(pages['entries'].path, function (data) {
    $('.entries-list').append(data)
  })
  pages['entries'].isSetup = true
}

var addEnties = function () {
  $.get(pages['entries'].path, function (data) {
    $('.entries-list').append(data)
  })
}

$(document).ready(function () {
  setupLogin()
  setupEditor()
  setupEntries()
  if (isUserLogin()) {
    changePage('editor')
  }else {
    changePage('login')
  }
})
