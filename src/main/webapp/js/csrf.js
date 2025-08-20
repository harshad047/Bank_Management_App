document.addEventListener('DOMContentLoaded', function () {
  var meta = document.querySelector('meta[name="csrf-token"]');
  var token = meta && meta.getAttribute('content');
  if (!token) return;
  var forms = document.querySelectorAll('form[method="post"], form[method="POST"]');
  forms.forEach(function (form) {
    if (!form.querySelector('input[name="csrfToken"]')) {
      var input = document.createElement('input');
      input.type = 'hidden';
      input.name = 'csrfToken';
      input.value = token;
      form.appendChild(input);
    }
  });
});

