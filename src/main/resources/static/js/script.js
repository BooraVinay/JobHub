
        function customPartialMatch(fullText, partialText) {
            // Create a regular expression with the partial text and the 'i' flag for case-insensitivity
            var regex = new RegExp(partialText, 'i');

            // Test if the full text matches the regular expression
            return regex.test(fullText);
        }

        // Function to handle the search
        function handleSearch() {
            var searchInput = document.getElementById('searchInput');
            var searchTerm = searchInput.value.trim().toLowerCase();

            // Get all job cards
            var jobCards = document.querySelectorAll('.job-card');

            // Iterate through job cards and show/hide based on the search term
            jobCards.forEach(function (jobCard) {
                var jobTitle = jobCard.querySelector('.job-title').textContent.toLowerCase();
                var jobCompany = jobCard.querySelector('.job-company').textContent.toLowerCase();
                var match = customPartialMatch(jobTitle, searchTerm) || customPartialMatch(jobCompany, searchTerm);

                // Toggle visibility based on matching
                jobCard.style.display = match ? 'block' : 'none';

                // Highlight matching text in title
                var titleSpan = jobCard.querySelector('.job-title');
                titleSpan.innerHTML = titleSpan.textContent.replace(new RegExp('(' + searchTerm + ')', 'ig'), '<span class="highlight">$1</span>');

                // Highlight matching text in company
                var companySpan = jobCard.querySelector('.job-company');
                companySpan.innerHTML = companySpan.textContent.replace(new RegExp('(' + searchTerm + ')', 'ig'), '<span class="highlight">$1</span>');
            });
        }

        function handleSort() {
            var sortOption = document.getElementById('sortOptions').value;
            var jobCardsContainer = document.getElementById('jobCardsContainer');
            var jobCards = jobCardsContainer.querySelectorAll('.job-card');

            var sortedJobCards = Array.from(jobCards).sort(function (a, b) {
                var dateA = getDateValue(a.querySelector('.job-date').textContent);
                var dateB = getDateValue(b.querySelector('.job-date').textContent);

                if (sortOption === 'mostRecent') {
                    return compareDates(dateB, dateA);
                } else {
                    return compareDates(dateA, dateB);
                }
            });

            // Clear current job cards and append sorted ones
            jobCardsContainer.innerHTML = '';
            sortedJobCards.forEach(function (jobCard) {
                jobCardsContainer.appendChild(jobCard.cloneNode(true));
            });
        }

        // Helper function to compare dates, handling cases where a date might be undefined
        function compareDates(dateA, dateB) {
            if (dateA && dateB) {
                return dateA - dateB;
            } else if (dateA) {
                return -1;
            } else if (dateB) {
                return 1;
            } else {
                return 0; // Both dates are undefined
            }
        }

        // Helper function to extract a date value from the job date text
        function getDateValue(dateText) {
            // Example: Assuming the date is in a simple format like "YYYY-MM-DD"
            // You may need to adjust this based on your actual date format
            var dateParts = dateText.split('-');
            if (dateParts.length === 3) {
                var year = parseInt(dateParts[0], 10);
                var month = parseInt(dateParts[1], 10);
                var day = parseInt(dateParts[2], 10);

                // Return a Date object or a numeric representation of the date
                // depending on your sorting needs
                return new Date(year, month - 1, day);
            } else {
                // Return undefined if the date format is not as expected
                return undefined;
            }
        }
