require "0_utils"

gradeCount = 0
TARGET_GRADES = 5

dofile("students.lua")
dofile("subjects.lua")
if not students then students = {} end
if not subjects then subjects = {} end

wrk.method = "POST"
wrk.headers["Content-Type"] = "application/json"
wrk.headers["Host"] = "localhost:8080"

function response(status, headers, body)
    if status == 200 or status == 201 then
        gradeCount = gradeCount + 1
    else
        print("Ошибка сервера: статус " .. status .. ", тело: " .. body)
    end
end

function request()
    if gradeCount < TARGET_GRADES then
        if #students == 0 or #subjects == 0 then
            print("Ошибка: нет доступных студентов или предметов")
            return nil
        end
        local path = "/api/grades"
        local studentId = students[math.random(1, #students)]
        local subjectId = subjects[math.random(1, #subjects)]
        local body = string.format([[
        {
          "student": {"id": "%s"},
          "subject": {"id": "%s"},
          "grade": %d,
          "dateReceived": "%s"
        }
        ]], studentId, subjectId, math.random(1, 100), randomDate())
        return wrk.format("POST", path, nil, body)
    end
    return nil
end