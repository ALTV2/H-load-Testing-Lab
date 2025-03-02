-- Define a function to generate random strings for names
function randomString(length)
    local res = ""
    for i = 1, length do
        res = res .. string.char(math.random(97, 122))
    end
    return res
end

-- Define a function to generate a random email
function randomEmail()
    return randomString(5) .. "@example.com"
end

-- Define a function to generate a random phone number
function randomPhone()
    return "+1" .. string.format("%010d", math.random(0, 9999999999))
end

function request()
    path = "/api/students"
    headers = {}
    headers["Host"] = "localhost:8080"
    headers["Content-Type"] = "application/json"

        -- Generate random data for the student
        local firstName = randomString(5)
        local lastName = randomString(5)
        local email = randomEmail()
        local phone = randomPhone()

        -- Prepare the JSON body for the POST request
        local body = string.format([[
        {
          "firstName": "%s",
          "lastName": "%s",
          "birthDate": "2001-01-01",
          "group": {
            "id": "-472c56d6-d1bb-4d0a-aa06-3aafdeaa2638"
          },
          "email": "%s",
          "phone": "%s"
        }
        ]], firstName, lastName, email, phone)

    return wrk.format("POST", path, headers, body)
end

-- Initialize wrk2 with a seed for repeatable randomness if needed
math.randomseed(os.time())