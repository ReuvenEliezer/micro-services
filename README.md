# Admin
- to see all the instances open: http://localhost:8080/applications
- u can to change the log level (by class) in log section
# zipkin (tracing logs)
- run docker-compose
- to see monitoring in UI open: http://localhost:9411/zipkin/
# Converter Controller 
- swagger http://localhost:9091/swagger-ui/index.html#/
- use this api to see trace-id & span-id 
- curl -X 'GET' \
  'http://localhost:9091/convert/call-aggregate-service' \
  -H 'accept: */*'
# Aggregation Controller
- swagger http://localhost:8081/swagger-ui/index.html#/
# Dependabot
- upgrade all dependencies (including parent) and docker image to keep us up-to-date
# github-actions
- build and run tests on master branch on PR or push.
- if one of the tests are failed - the build will be failed
- u can see tests result under "actions" in github repository https://github.com/ReuvenEliezer/NiceMicroServices/actions 
