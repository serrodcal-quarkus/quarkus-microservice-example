// k6 run --vus 20 --duration 1m script.js

import http from 'k6/http';
import { check } from 'k6';
export default function () {
  var payload = JSON.stringify({
    name: 'foo',
    surname: 'bar',
    birth: '1989-12-23',
    email: 'foo@bar.com',
  });

  var params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  let post_res = http.post('http://localhost:8080/api/v1/employee', payload, params);

  let id = post_res.json("id");

  let get_res = http.get('http://localhost:8080/api/v1/employee/' + id.toString());

  let del_res = http.del('http://localhost:8080/api/v1/employee/' + id.toString());

  check(post_res, { 'status was 201': (r) => r.status == 201 });
  check(get_res, { 'status was 200': (r) => r.status == 200 });
  check(del_res, { 'status was 204': (r) => r.status == 204 });
}